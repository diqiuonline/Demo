
var dbDefJdbcParam = {
		
	oracle: {
		JdbcDriver: "oracle.jdbc.driver.OracleDriver",
	    JdbcURL: "jdbc:oracle:thin:@127.0.0.1:1521:db_name",
		Charset:"",
		UseChainedTransaction: true,
		validConnSql:"select 1 from dual"
	},
	
	sqlserver: {
		JdbcDriver: "com.microsoft.jdbc.sqlserver.SQLServerDriver",
		JdbcURL: "jdbc:microsoft:sqlserver://127.0.0.1:1433;DatabaseName=db_name;SelectMethod=cursor",
		Charset:"",
		UseChainedTransaction: true,
		validConnSql:"select getdate()"
	},
	
	sybase: {
		JdbcDriver: "com.sybase.jdbc.SybDriver",
		JdbcURL: "jdbc:sybase:Tds:127.0.0.1:5000/db_name",
		Charset: "cp936",
		UseChainedTransaction: false,
		validConnSql:"select getdate()"
	},
	
	db2: {
		JdbcDriver: "com.ibm.db2.jcc.DB2Driver",
		JdbcURL: "jdbc:db2://127.0.0.1:50000/db_name",
		Charset: "",
		UseChainedTransaction: true,
		validConnSql:"select  1 from sysibm.dual"
	},
	
//	mysql: {
//		JdbcDriver: "com.mysql.jdbc.Driver",
//		JdbcURL: "jdbc:mysql://127.0.0.1:3306/db_name",
//		//JdbcURL: "jdbc:mysql://168.1.194.248:3306/platform",
//		Charset: "",
//		UseChainedTransaction: true,
//		validConnSql:"select 1"
//	},
	
	sqlserver2005: {
		JdbcDriver: "com.microsoft.sqlserver.jdbc.SQLServerDriver",
		JdbcURL: "jdbc:sqlserver://127.0.0.1:1433;DatabaseName=db_name;SelectMethod=direct",
		Charset: "",
		UseChainedTransaction: true,
		validConnSql:"select getdate()"
	},
	
	mysql5: {
		JdbcDriver: "com.mysql.jdbc.Driver",
		JdbcURL: "jdbc:mysql://127.0.0.1:3306/db_name",
		Charset: "",
		UseChainedTransaction: true,
		validConnSql:"select 1"
	},
	mysql8: {
		JdbcDriver: "com.mysql.cj.jdbc.Driver",
		JdbcURL: "jdbc:mysql://127.0.0.1:3306/db_name?useSSL=false&nullCatalogMeansCurrent=true",
		Charset: "",
		UseChainedTransaction: true,
		validConnSql:"select 1"
	},
	DM: {
		JdbcDriver: "dm.jdbc.driver.DmDriver",
		JdbcURL: "jdbc:dm://127.0.0.1:5236/dbname",
		Charset: "",
		UseChainedTransaction: true,
		validConnSql:"select sysdate()"
	},
	KingBase:{
		JdbcDriver: "com.kingbase.Driver",
		JdbcURL: "jdbc:kingbase://127.0.0.1:54321/dbname",
		Charset: "",
		UseChainedTransaction: true,
		validConnSql:"select sysdate"
	},
	gbase8t:{
		JdbcDriver: "com.informix.jdbc.IfxDriver",
		JdbcURL: "jdbc:informix-sqli://127.0.0.1:1526/dbname:informixserver=servername",
		Charset: "",
		UseChainedTransaction: true,
		validConnSql:"select sysdate from systables"
	} ,
	gbase8s:{
		JdbcDriver: "com.informix.jdbc.IfxDriver",
		JdbcURL: "jdbc:informix-sqli://127.0.0.1:1526/dbname:informixserver=servername",
		Charset: "",
		UseChainedTransaction: true,
		validConnSql:"select sysdate from systables"
		//JdbcDriver: "com.gbase.jdbc.Driver",
		//JdbcURL: "jdbc:GBase://localhost:5258/dbname",
		//Charset: "",
		//UseChainedTransaction: true,
		//validConnSql:"select sysdate()"
	},
	oscar:{
		JdbcDriver: "com.oscar.Driver",
		JdbcURL: "jdbc:oscar://127.0.0.1:2003/dbname",
		Charset: "",
		UseChainedTransaction: true,
		validConnSql:"select sysdate()"
	},
	sr:{
		JdbcDriver: "org.srdbsql.Driver",
		JdbcURL: "jdbc:srdbsql://localhost:1975/dbname",
		Charset: "",
		UseChainedTransaction: true,
		validConnSql:"select sysdate()"
	},
	mppdb:{
		JdbcDriver: "org.postgresql.Driver",
		JdbcURL: "jdbc:postgresql://127.0.0.1:25308/dbname",
		rset: "",
		UseChainedTransaction: true,
		validConnSql:"select sysdate"
	},
	tdh:{
		JdbcDriver: "io.transwarp.jdbc.InceptorDriver",
		JdbcURL: "jdbc:hive2://127.0.0.1:30797/dbname;guardianToken=",
		rset: "",
		UseChainedTransaction: true,
		validConnSql:"SELECT sysdate FROM system.dual"
	}
};