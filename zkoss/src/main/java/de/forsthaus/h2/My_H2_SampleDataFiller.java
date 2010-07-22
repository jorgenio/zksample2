/**
 * Copyright 2010 the original author or authors.
 * 
 * This file is part of Zksample2. http://zksample2.sourceforge.net/
 *
 * Zksample2 is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * Zksample2 is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Zksample2.  If not, see <http://www.gnu.org/licenses/gpl.html>.
 */
package de.forsthaus.h2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.BatchUpdateException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;

/**
 * This database filler is called by spring and fills the db with sample data.
 * 
 * @author bbruhns
 * @author sgerth
 */
public class My_H2_SampleDataFiller implements InitializingBean {

	private DataSource dataSource;

	public void afterPropertiesSet() throws Exception {
		Logger logger = Logger.getLogger(getClass());
		Map<Integer, String> allSql = new HashMap<Integer, String>();
		Connection conn = dataSource.getConnection();
		try {
			// reads the sql-file from the classpath
			InputStream inputStream = getClass().getResourceAsStream("/createSampleData.sql");
			try {

				Statement stat = conn.createStatement();

				BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
				String str = "";
				StringBuilder sb = new StringBuilder();
				int count = 0;
				while ((str = in.readLine()) != null) {
					sb.append(str);
					// make a linefeed at each readed line
					if (StringUtils.endsWith(str.trim(), ";")) {
						String sql = sb.toString();
						stat.addBatch(sql);
						sb = new StringBuilder();
						allSql.put(Integer.valueOf(count++), sql);
					} else {
						sb.append("\n");
					}
				}

				int[] ar = stat.executeBatch();
				int i = ar.length;

				logger.info("Create DemoData");
				logger.info("count batch updates : " + i);

			} finally {
				try {
					inputStream.close();
				} catch (IOException e) {
					logger.warn("", e);
				}
			}
		} catch (BatchUpdateException e) {
			BatchUpdateException be = e;
			int[] updateCounts = be.getUpdateCounts();
			if (updateCounts != null) {
				for (int i = 0; i < updateCounts.length; i++) {
					int j = updateCounts[i];
					if (j < 0) {
						logger.error("SQL errorcode: " + j + " -> in SQL\n" + allSql.get(Integer.valueOf(i)));
					}
				}
			}
			throw e;
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				logger.warn("", e);
			}
		}
	}

	public DataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
}
