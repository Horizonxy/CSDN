package com.horizon.csdn.db;

import android.content.Context;

import com.horizon.csdn.vo.CommonCacheVo;

import java.sql.SQLException;

public class CommonDaoImpl extends BaseDaoImpl<CommonCacheVo, Long> {

    public CommonDaoImpl(Context context) {
        super(context, CommonCacheVo.class);
    }

    public void deleteAll(){
        try {
            super.deleteAll(CommonCacheVo.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
