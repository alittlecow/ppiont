package com.core.support;

import com.core.pojo.TimeRange;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class SqlBuilder {
    private StringBuilder sqlStr = new StringBuilder();

    private ArrayList params = new ArrayList();

    private boolean hasWhereCondition = false;

    private static final String likeKey = "like";

    public String getSqlStr() {
        return sqlStr.toString();
    }


    public SqlBuilder addSql(String sqlStr) {
        this.sqlStr.append(" ").append(sqlStr);
        return this;
    }

    public SqlBuilder setHasWhereCondition(boolean hasWhereCondition) {
        this.hasWhereCondition = hasWhereCondition;
        return this;
    }

    public Object[] getSqlParams() {
        return params.toArray();
    }

    public SqlBuilder addParam(Object obj) {
        params.add(obj);
        return this;
    }

    public SqlBuilder addWhere(String field, String operator, Object value) {
        if (value == null) return this;
        if(value instanceof String && StringUtils.isBlank((String) value))return this;
        if (hasWhereCondition)
            sqlStr.append(" and ");
        else
            hasWhereCondition = true;
        sqlStr.append(" ").append(field).append(" ").append(operator).append(" ? ");
        addParam(value);
        return this;
    }

    public SqlBuilder notEq(String field, Object value) {
        addWhere(field, "<>", value);
        return this;
    }

    public SqlBuilder eq(String field, Object value) {
        addWhere(field, "=", value);
        return this;
    }

    public SqlBuilder ne(String field, Object value) {
        addWhere(field, "<>", value);
        return this;
    }

    public SqlBuilder gt(String field, Object value) {
        addWhere(field, ">", value);
        return this;
    }

    public SqlBuilder lt(String field, Object value) {
        addWhere(field, "<", value);
        return this;
    }

    public SqlBuilder ge(String field, Object value) {
        addWhere(field, ">=", value);
        return this;
    }

    public SqlBuilder le(String field, Object value) {
        addWhere(field, "<=", value);
        return this;
    }

    public SqlBuilder like(String field, Object value) {
        addWhere(field, "like", "%" + value + "%");
        return this;
    }

    public SqlBuilder in(String field, List<Object> list) {
        if (CollectionUtils.isEmpty(list)) return this;
        if (hasWhereCondition)
            sqlStr.append(" and ");
        sqlStr.append(" ").append(field).append(" in (");
        hasWhereCondition = true;
        int size = list.size();
        for (int i = 0; i < size; i++) {
            addParam(list.get(i));
            sqlStr.append((i == size - 1) ? "?) " : "?,");
        }
        return this;
    }

    public SqlBuilder likeIfNotEmpty(String field, String value) {
        if (StringUtils.isBlank(value)) return this;
        like(field, value);
        return this;
    }

    public SqlBuilder between(String field, Object start, Object end) {
        this.gt(field, start).lt(field, end);
        return this;
    }

    public SqlBuilder betweenIn(String field, Object start, Object end) {
        this.ge(field, start).le(field, end);
        return this;
    }

    public SqlBuilder betweenDay(String field, TimeRange timeRange) {
        if (timeRange == null)
            return this;
        if (timeRange.getStartTime() != null)
            this.gtDay(field, timeRange.getStartTime());
        if (timeRange.getEndTime() != null) {
            this.ltDay(field,timeRange.getEndTime());
        }
        return this;
    }


    /**
     * 大于某天的开始时间 比如 2016-8-26  14：44：01
     * 为 >2016-8-26  00：00：00 000
     *
     * @param field
     * @param value
     * @return
     */
    public SqlBuilder gtDay(String field, Date value) {
        if (value == null) return this;
        this.gt(field, DateUtils.truncate(value, Calendar.DATE));
        return this;
    }

    /**
     * 小于某天的结束时间 比如 2016-8-26  14：44：01
     * 为 <2016-8-27  00：00：00 000
     *
     * @param field
     * @param value
     * @return
     */
    public SqlBuilder ltDay(String field, Date value) {
        if (value == null) return this;
        value = DateUtils.truncate(value, Calendar.DATE);
        value = DateUtils.addDays(value, 1);
        this.lt(field, value);
        return this;
    }


    public void addUpdateField(String field, Object value) {
        if (value == null) return;
        sqlStr.append(" ").append(field).append("=?,");
        params.add(value);
    }

    public void endUpdateFields() {
        sqlStr.deleteCharAt(sqlStr.length() - 1).append(" ");
    }
}
