package com.subaiqiao.androidutils.utils.db

object Tables {

    val CREATE_SYSTEM_CONFIG_TABLE = "CREATE TABLE t_system_config (" +
            "  id text NOT NULL," +
            "  code text," +
            "  value text," +
            "  is_delete integer NOT NULL DEFAULT 0," +
            "  PRIMARY KEY (id)" +
            ");"
}