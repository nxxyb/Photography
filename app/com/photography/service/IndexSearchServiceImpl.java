package com.photography.service;

import java.util.List;

import org.apache.lucene.search.SortField;
import org.hibernate.Query;
import org.hibernate.criterion.Projections;
import org.hibernate.search.FullTextQuery;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.Search;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.springframework.stereotype.Service;

import com.photography.dao.query.Pager;
import com.photography.dao.query.Sort;
import com.photography.mapping.User;

/**
 * 索引查询
 * @author Administrator
 *
 */
@Service("indexSearchService")
public class IndexSearchServiceImpl extends BaseServiceImpl implements IIndexSearchService {

	@SuppressWarnings("unchecked")
	public <T> List<T> getIndexPojoList(Class<T> clazz, Pager pager,String[] keys, String searchString, Sort sort, User user){
		
		FullTextSession fullTextSession = Search.getFullTextSession(this.hibernateDao.getHibernateTemplate());
		
		QueryBuilder queryBuilder = fullTextSession.getSearchFactory().buildQueryBuilder().forEntity( clazz ).get();
		
		org.apache.lucene.search.Query luceneQuery = queryBuilder.keyword().onFields(keys).matching(searchString).createQuery();
		
		FullTextQuery  hibQuery = fullTextSession.createFullTextQuery(luceneQuery, clazz);
		
		//计算总数
		if (pager != null) {
			int count = hibQuery.getResultSize();
//			int allPage = count/pager.getPageSize();
			pager.setTotalCount(count+pager.getOffset());
//			if(pager.getCurrentPage()>pager.getTotalPage()) {
//				pager.setCurrentPage(pager.getTotalPage());
//			}
		}
		
		//排序
		if (sort != null && !sort.isEmpty()) {
			
			org.apache.lucene.search.Sort sortLucene = new org.apache.lucene.search.Sort();
			boolean reverse = false;
			if(!"ASC".equalsIgnoreCase(sort.getSortItemList().get(0).getOrder())){
				reverse = true;
			}
			sortLucene.setSort(new SortField(sort.getSortItemList().get(0).getFieldName(), SortField.STRING,reverse));
			hibQuery.setSort(sortLucene);
		}
		
		if (pager != null) {
//			if (pager.getTotalCount() <= pager.getFromRowIndex()) {
//				pager.setCurrentPage(1);
//			}
			if(pager.getCurrentPage()==1) {
				hibQuery.setFirstResult(pager.getFromRowIndex());
				hibQuery.setMaxResults(pager.getPageSize()-pager.getOffset());
			} else {
				hibQuery.setFirstResult(pager.getFromRowIndex()-pager.getOffset());
				hibQuery.setMaxResults(pager.getPageSize());
			}
		}
		return hibQuery.list();
	}

	@Override
	public void createIndex() {
		FullTextSession fullTextSession = Search.getFullTextSession(this.hibernateDao.getHibernateTemplate());
		
		try {
			fullTextSession.createIndexer().startAndWait();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
