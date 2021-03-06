package com.qingmei2.module_a.mvp.model;

import android.support.annotation.VisibleForTesting;

import com.qingmei2.module.base.BaseModel;
import com.qingmei2.module.http.entity.UserInfo;
import com.qingmei2.module.http.service.ServiceManager;
import com.qingmei2.module_a.mvp.contract.HomeContract;

import javax.inject.Inject;

import io.reactivex.Maybe;
import io.reactivex.MaybeTransformer;
import io.rx_cache2.DynamicKey;
import io.rx_cache2.EvictDynamicKey;

/**
 * Created by QingMei on 2017/8/14.
 * desc:
 */

public class HomeModel extends BaseModel<ServiceManager> implements HomeContract.Model {

    @Inject
    public HomeModel(ServiceManager serviceManager) {
        super(serviceManager);
    }

    @Override
    public Maybe<UserInfo> requestUserInfo(final String userName) {
        return serviceManager.getUserInfoService()
                .getUserInfo(userName)
                .compose(getUserInfoCache(userName, false))
                .subscribeOn(schedulers.io())
                .observeOn(schedulers.ui());
    }

    @VisibleForTesting
    public MaybeTransformer<UserInfo, UserInfo> getUserInfoCache(final String dynamicKey, final boolean refresh) {
        return maybe -> cacheProviders
                .getUserInfoCacheProviders()
                .getUserInfo(maybe, new DynamicKey(dynamicKey), new EvictDynamicKey(refresh));
    }
}
