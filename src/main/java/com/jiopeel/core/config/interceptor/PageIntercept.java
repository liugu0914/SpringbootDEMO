package com.jiopeel.core.config.interceptor;

import com.jiopeel.core.bean.Page;
import com.jiopeel.core.util.BaseUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.executor.resultset.ResultSetHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.apache.ibatis.scripting.defaults.DefaultParameterHandler;

import java.sql.*;
import java.util.List;
import java.util.Properties;

/**
 * Mybatis -分页插件（如果开启二级缓存需要注意）
 *
 * @author ：lyc
 * @date ：2019/12/24 18:35
 */
@Intercepts({@Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class, Integer.class}),
        @Signature(type = ResultSetHandler.class, method = "handleResultSets", args = {Statement.class})})
@Slf4j
public class PageIntercept implements Interceptor {

    public static final ThreadLocal<Page> localPage = new ThreadLocal<Page>();

    public static final String LMT_TABLE_NAME = "_lyc_lmt_";

    public static final String ROW_NAME = "_lyc_row_";

    private String dbType;

    /**
     * 开始分页
     *
     * @param page
     */
    public static <E> void startPage(Page<E> page) {
        if (BaseUtil.empty(page))
            page = new Page<E>();
        localPage.set(page);

    }

    /**
     * 结束分页并返回结果，该方法必须被调用，否则localPage会一直保存下去，直到下一次startPage
     *
     * @return
     */
    public static Page endPage() {
        Page page = localPage.get();
        localPage.remove();
        return page;
    }

    /**
     * 代理对象
     *
     * @param invocation
     * @return Object
     * @throws Throwable
     */
    public Object intercept(Invocation invocation) throws Throwable {
        if (localPage.get() == null) {
            return invocation.proceed();
        }
        if (invocation.getTarget() instanceof StatementHandler) {
            StatementHandler statementHandler = (StatementHandler) invocation.getTarget();
            MetaObject metaStatementHandler = SystemMetaObject.forObject(statementHandler);
            // 分离代理对象链(由于目标类可能被多个插件拦截，从而形成多次代理，通过下面的两次循环
            // 可以分离出最原始的的目标类)
            while (metaStatementHandler.hasGetter("h")) {
                Object object = metaStatementHandler.getValue("h");
                metaStatementHandler = SystemMetaObject.forObject(object);
            }
            // 分离最后一个代理对象的目标类
            while (metaStatementHandler.hasGetter("target")) {
                Object object = metaStatementHandler.getValue("target");
                metaStatementHandler = SystemMetaObject.forObject(object);
            }
            MappedStatement mappedStatement = (MappedStatement) metaStatementHandler.getValue("delegate.mappedStatement");
            BoundSql boundSql = (BoundSql) metaStatementHandler.getValue("delegate.boundSql");
            // 分页参数作为参数对象parameterObject的一个属性
            String sql = boundSql.getSql();
            if (!checkIsSelect(sql) || sql.indexOf(LMT_TABLE_NAME) > 0)
                return invocation.proceed();
            Connection connection = (Connection) invocation.getArgs()[0];
            this.dbType = getDBType(connection);
            //分页信息
            Page page = localPage.get();
            // 重写sql
            String pageSql = buildPageSql(sql, page);
            metaStatementHandler.setValue("delegate.boundSql.sql", pageSql);
            // 重设分页参数里的总页数等
            setPageParameter(sql, connection, mappedStatement, boundSql, page);
            // 将执行权交给下一个插件
            return invocation.proceed();
        } else if (invocation.getTarget() instanceof ResultSetHandler) {
            Object result = invocation.proceed();
            Page page = localPage.get();
            page.setResult((List) result);
            return result;
        }
        return null;
    }

    /**
     * 通过Connection获取数据库类型
     *
     * @param connection
     * @return String
     */
    private String getDBType(Connection connection) throws SQLException {
        //通过driverName是否包含关键字判断
        if (connection.getMetaData().getDriverName().toUpperCase().indexOf("MYSQL") != -1) {
            return "mysql";
        } else if (connection.getMetaData().getDriverName().toUpperCase().indexOf("SQL SERVER") != -1) {
            return "sqlserver";
        } else if (connection.getMetaData().getDriverName().toUpperCase().indexOf("ORACLE") != -1) {
            return "oracle";
        }
        return "mysql";
    }

    /**
     * 只拦截这两种类型的
     * <br>StatementHandler
     * <br>ResultSetHandler
     *
     * @param target
     * @return
     */
    public Object plugin(Object target) {
        if (target instanceof StatementHandler || target instanceof ResultSetHandler) {
            return Plugin.wrap(target, this);
        } else {
            return target;
        }
    }

    /**
     * 设置初始参数
     *
     * @param properties
     */
    public void setProperties(Properties properties) {
        //读取设置的数据库类型
        this.dbType = properties.getProperty("dbType", "mysql").toLowerCase();
    }

    /**
     * 修改原SQL为分页SQL
     *
     * @param sql
     * @param page
     * @return String
     */
    private String buildPageSql(String sql, Page page) {
        StringBuilder pageSql = new StringBuilder();
        switch (this.dbType) {
            case "mysql":
                pageSql.append("select * from (");
                pageSql.append(sql);
                pageSql.append(") " + LMT_TABLE_NAME);
                pageSql.append(String.format(" limit %d,%d ", page.getStartRow(), page.getPageSize()));
                break;
            case "sqlserver"://适用Sql Server 2012 版本以上
                pageSql.append("select *,1 as" + ROW_NAME + " from (");
                pageSql.append(sql);
                pageSql.append(") " + LMT_TABLE_NAME);
                pageSql.append(String.format(" order by " + ROW_NAME + " offset %d rows fetch next %d rows only", page.getStartRow(), page.getPageSize()));
                break;
            case "oracle":
                int endResult = page.getStartRow() + page.getPageSize();
                sql = "select *,rownum  " + ROW_NAME + " from (" + sql + ") where rownum <=" + endResult;
                pageSql.append("select * from (" + sql + ") " + LMT_TABLE_NAME + " where _lyc_row >" + page.getStartRow());
                break;
        }
        return pageSql.toString();
    }

    /**
     * 获取总记录数
     *
     * @param sql
     * @param connection
     * @param mappedStatement
     * @param boundSql
     * @param page
     */
    private void setPageParameter(String sql, Connection connection, MappedStatement mappedStatement,
                                  BoundSql boundSql, Page page) {
        // 记录总记录数
        String countSql = "select count(0) from (" + sql + ") " + LMT_TABLE_NAME;
        PreparedStatement countStmt = null;
        ResultSet rs = null;
        try {
            countStmt = connection.prepareStatement(countSql);
            BoundSql countBS = new BoundSql(mappedStatement.getConfiguration(), countSql,
                    boundSql.getParameterMappings(), boundSql.getParameterObject());
            setParameters(countStmt, mappedStatement, countBS, boundSql.getParameterObject());
            rs = countStmt.executeQuery();
            int totalCount = 0;
            if (rs.next()) {
                totalCount = rs.getInt(1);
            }
            page.setTotal(totalCount);
            int totalPage = totalCount / page.getPageSize() + ((totalCount % page.getPageSize() == 0) ? 0 : 1);
            page.setPages(totalPage);
        } catch (SQLException e) {
            log.error("Ignore this exception", e);
        } finally {
            try {
                rs.close();
            } catch (SQLException e) {
                log.error("Ignore this exception", e);
            }
            try {
                countStmt.close();
            } catch (SQLException e) {
                log.error("Ignore this exception", e);
            }
        }
    }

    /**
     * 代入参数值
     *
     * @param ps
     * @param mappedStatement
     * @param boundSql
     * @param parameterObject
     * @throws SQLException
     */
    private void setParameters(PreparedStatement ps, MappedStatement mappedStatement, BoundSql boundSql,
                               Object parameterObject) throws SQLException {
        ParameterHandler parameterHandler = new DefaultParameterHandler(mappedStatement, parameterObject, boundSql);
        parameterHandler.setParameters(ps);
    }


    /**
     * 判断是否是select语句，只有select语句，才会用到分页
     *
     * @return ： 是则返回true 否则返回false
     * @author ：lyc
     * @date ：2019/12/24 15:39
     */
    private boolean checkIsSelect(String sql) {
        String trimSql = sql.trim();
        int index = trimSql.toLowerCase().indexOf("select");
        return index == 0;
    }
}