package com.coredump.socialdump.service;

import com.coredump.socialdump.domain.SearchCriteria;
import com.coredump.socialdump.domain.SocialNetworkPost;

/**
 * Created by Franz on 15/07/2015.
 */
//¡Esta interfaz tiene un nombre un correcto!. O no [inserte nombre]?
public interface FetchableInterface extends Runnable{

    @Override
    void run();

    void setSearchCriteria(SearchCriteria searchCriteria);
}
