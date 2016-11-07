package com.core.support;

import com.core.pojo.PageReq;
import com.core.pojo.Pagination;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @version 1.0.0
 */
@Repository
public class BaseHibernateDao extends HibernateTemplate {
    final static Pattern SQL_FIELDS = Pattern.compile(
            "\\W*select\\W+(.+)\\W+(from.*)", Pattern.CASE_INSENSITIVE);
    final static String SQL_ORDER = " (?i)order\\W+by.+$";
    final static Pattern SQL_GROUP = Pattern.compile("\\W+group\\W+by", Pattern.CASE_INSENSITIVE);

    @Autowired
    public BaseHibernateDao(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public <T> T uniqueResultByHql(final String hql, final Object... params) {
        return this.execute(new HibernateCallback<T>() {
            @SuppressWarnings("unchecked")
            public T doInHibernate(Session session) {
                Query query = session.createQuery(hql);
                if (params != null) {
                    for (int i = 0; i < params.length; i++) {
                        query.setParameter(i, params[i]);
                    }
                }
                return (T) query.setMaxResults(1).uniqueResult();
//				List list = query.list();
//				return list.isEmpty()?null: (T) list.get(0);
            }
        });
    }

    public <T> T uniqueResultBySql(final String sql, final Class<T> resultType, final Object... params) {
        return this.execute(new HibernateCallback<T>() {
            @SuppressWarnings("unchecked")
            public T doInHibernate(Session session) {
                Query query = session.createSQLQuery(sql);
                if (params != null) {
                    for (int i = 0; i < params.length; i++) {
                        query.setParameter(i, params[i]);
                    }
                }
                if (resultType != null) {
                    query.setResultTransformer(new AliasToBeanResultTransformer(
                            resultType));
                }
                return (T) query.setMaxResults(1).uniqueResult();
//				List list = query.list();
//				return list.isEmpty()?null: (T) list.get(0);
            }
        });
    }

    public <T> List<T> findByHql(final String hql, int pageNo,
                                 final int onePageNum, final Object... params) {
        final int offset = pageNo * onePageNum;
        return this.execute(new HibernateCallback<List<T>>() {
            public List<T> doInHibernate(Session session) {
                Query query = session.createQuery(hql);
                if (params != null) {
                    for (int i = 0; i < params.length; i++) {
                        query.setParameter(i, params[i]);
                    }
                }

                if (onePageNum > 0 && offset >= 0) {
                    query.setFirstResult(offset);
                    query.setMaxResults(onePageNum);
                }
                return query.list();
            }
        });
    }


    public <T> List<T> findByHql(final String hql, final Object... params) {
        return this.execute(new HibernateCallback<List<T>>() {
            public List<T> doInHibernate(Session session) {
                Query query = session.createQuery(hql);
                if (params != null) {
                    for (int i = 0; i < params.length; i++) {
                        query.setParameter(i, params[i]);
                    }
                }
                return query.list();
            }
        });
    }

    public <T> List<T> findForMaxByHql(final String hql, final int max, final Object... params) {
        return this.execute(new HibernateCallback<List<T>>() {
            public List<T> doInHibernate(Session session) {
                Query query = session.createQuery(hql);
                if (params != null) {
                    for (int i = 0; i < params.length; i++) {
                        query.setParameter(i, params[i]);
                    }
                }
                query.setFirstResult(0);
                query.setMaxResults(max);
                return query.list();
            }
        });
    }


    public <T> Pagination<T> findPageByHql(final String hql,
                                           final PageReq page, final Object... params) {
        final int offset = page.pageNo * page.onePageNum;
        return this.execute(new HibernateCallback<Pagination<T>>() {
            public Pagination<T> doInHibernate(Session session) {
                String countHql = parseForCountSql(hql);
                int totalCount = countByHql(countHql, params);
                if (totalCount == 0) {
                    return new Pagination<T>(totalCount, page.pageNo,
                            page.onePageNum, Collections.<T>emptyList());
                }
                Query query = session.createQuery(hql);
                if (params != null) {
                    for (int i = 0; i < params.length; i++) {
                        query.setParameter(i, params[i]);
                    }
                }

                if (page.onePageNum > 0 && offset >= 0) {
                    query.setFirstResult(offset);
                    query.setMaxResults(page.onePageNum);
                }
                List<T> list = query.list();
                return new Pagination<T>(totalCount, page.pageNo,
                        page.onePageNum, list);
            }
        });
    }

    public <T> Pagination<T> findPageByHqlWithSubquery(final String dataHql, final String condHql,
                                                       final PageReq page, final Object... params) {
        final int offset = page.pageNo * page.onePageNum;
        return this.execute(new HibernateCallback<Pagination<T>>() {
            public Pagination<T> doInHibernate(Session session) {
                String countHql = parseForCountSql(condHql);
                int totalCount = countByHql(countHql, params);

                if (totalCount == 0) {
                    return new Pagination<T>(totalCount, page.pageNo,
                            page.onePageNum, Collections.<T>emptyList());
                }

                Query query = session.createQuery(dataHql + condHql);

                if (params != null) {
                    for (int i = 0; i < params.length; i++) {
                        query.setParameter(i, params[i]);
                    }
                }

                if (page.onePageNum > 0 && offset >= 0) {
                    query.setFirstResult(offset);
                    query.setMaxResults(page.onePageNum);
                }
                List<T> list = query.list();
                return new Pagination<T>(totalCount, page.pageNo,
                        page.onePageNum, list);
            }
        });
    }

    public <T> Pagination<T> findPageBySql(final String sql,
                                           final PageReq page, final Class<T> resultType, final Object... params) {

        return this.execute(new HibernateCallback<Pagination<T>>() {
            public Pagination<T> doInHibernate(Session session) {
                SQLQuery query = session.createSQLQuery(sql);
                if (resultType != null) {
                    query.setResultTransformer(new AliasToBeanResultTransformer(
                            resultType));
                }
                if (params != null) {
                    for (int i = 0; i < params.length; i++) {
                        query.setParameter(i, params[i]);
                    }
                }
                final int offset = page.pageNo * page.onePageNum;
                if (page.onePageNum > 0 && offset >= 0) {
                    query.setFirstResult(offset);
                    query.setMaxResults(page.onePageNum);
                }
                List<T> list = query.list();
                String countHql = parseForCountSql(sql);
                int totalCount = countBySql(countHql, params);
                return new Pagination<T>(totalCount, page.pageNo,
                        page.onePageNum, list);
            }
        });
    }

    public <T> Pagination<T> findPageBySql(final String sql,
                                           final int pageNo, final int onePageNum, final Class<T> resultType, final Object... params) {
        return this.execute(new HibernateCallback<Pagination<T>>() {
            public Pagination<T> doInHibernate(Session session) {
                SQLQuery query = session.createSQLQuery(sql);
                if (resultType != null) {
                    query.setResultTransformer(new AliasToBeanResultTransformer(
                            resultType));
                }
                if (params != null) {
                    for (int i = 0; i < params.length; i++) {
                        query.setParameter(i, params[i]);
                    }
                }
                final int offset = pageNo * onePageNum;
                if (onePageNum > 0 && offset >= 0) {
                    query.setFirstResult(offset);
                    query.setMaxResults(onePageNum);
                }
                List<T> list = query.list();
                String countHql = parseForCountSql(sql);
                int totalCount = countBySql(countHql, params);
                return new Pagination<T>(totalCount, pageNo,
                        onePageNum, list);
            }
        });
    }

    public <T> Pagination<T> findPageByHql(final String hql, final int pageNo,
                                           final int onePageNum, final Object... params) {
        final int offset = pageNo * onePageNum;
        return this.execute(new HibernateCallback<Pagination<T>>() {
            public Pagination<T> doInHibernate(Session session) {
                Query query = session.createQuery(hql);
                if (params != null) {
                    for (int i = 0; i < params.length; i++) {
                        query.setParameter(i, params[i]);
                    }
                }

                if (onePageNum > 0 && offset >= 0) {
                    query.setFirstResult(offset);
                    query.setMaxResults(onePageNum);
                }
                List<T> list = query.list();
                String countHql = parseForCountSql(hql);
                int totalCount = countByHql(countHql, params);
                return new Pagination<T>(totalCount, pageNo, onePageNum, list);
            }
        });
    }

    public int countByHql(final String hql, final Object... params) {
        return ((Number) find(hql, params).get(0)).intValue();
    }

    public int countBySql(final String sql, final Object... params) {
        return this.execute(new HibernateCallback<Integer>() {
            public Integer doInHibernate(Session session) {
                Query query = session.createSQLQuery(sql);
                if (params != null) {
                    for (int i = 0; i < params.length; i++) {
                        query.setParameter(i, params[i]);
                    }
                }
                return ((Number) query.uniqueResult()).intValue();
            }
        });
    }


    public <M> List<M> findBySql(final String sql, final Class<M> resultType,
                                 final Object... params) {
        return (List<M>) this.execute(new HibernateCallback<List<M>>() {
            public List<M> doInHibernate(Session session) {
                SQLQuery query = session.createSQLQuery(sql);
                if (resultType != null) {
                    query.setResultTransformer(new AliasToBeanResultTransformer(
                            resultType));
                }

                if (params != null) {
                    for (int i = 0; i < params.length; i++) {
                        query.setParameter(i, params[i]);
                    }
                }
                return query.list();
            }
        });
    }

    public List<?> findBySql(final String sql,
                             final Object... params) {
        return (List<?>) this.execute(new HibernateCallback<List<?>>() {
            public List<?> doInHibernate(Session session) {
                SQLQuery query = session.createSQLQuery(sql);
                if (params != null) {
                    for (int i = 0; i < params.length; i++) {
                        query.setParameter(i, params[i]);
                    }
                }
                return query.list();
            }
        });
    }


    public <M> List<M> findEntityBySql(final String sql, final int pageNo,
                                       final int onePageNum, final Class<M> entityType,
                                       final Object... params) {
        final int offset = pageNo * onePageNum;
        return this.execute(new HibernateCallback<List<M>>() {
            public List<M> doInHibernate(Session session) {
                SQLQuery query = session.createSQLQuery(sql).addEntity(
                        entityType);

                if (params != null) {
                    for (int i = 0; i < params.length; i++) {
                        query.setParameter(i, params[i]);
                    }
                }

                if (onePageNum > 0 && offset >= 0) {
                    query.setFirstResult(offset);
                    query.setMaxResults(onePageNum);
                }

                return query.list();
            }
        });
    }

    public int executeSql(final String sql) {
        return executeSql(sql, (Object[])null);
    }

    public int executeSql(final String sql, final Object... params) {
        return this.execute(new HibernateCallback<Integer>() {
            public Integer doInHibernate(Session session) {
                SQLQuery query = session.createSQLQuery(sql);

                if (params != null && params.length > 0) {
                    for (int i = 0; i < params.length; i++) {
                        query.setParameter(i, params[i]);
                    }
                }
                return query.executeUpdate();
            }
        });
    }

    /**
     * 不支持嵌套查询
     *
     * @param sql
     * @return
     */
    public static String parseForCountSql(String sql) {
        Matcher matcher = SQL_FIELDS.matcher(sql);
        String countSql;
        if (matcher.matches())
            countSql = "select count(*) "
                    + matcher.group(2).replaceAll(SQL_ORDER, "");
        else
            countSql = "select count(*) " + sql.replaceAll(SQL_ORDER, "");
        if (SQL_GROUP.matcher(countSql).find())
            countSql = "select count(*) from ( " + countSql + " ) _DUMMY_TABLE";
        return countSql;
    }

}
