package com.mickey.dinggading.domain.band.service;

import com.mickey.dinggading.model.*;

public interface BandService {

    // 밴드 관련 기능
    BandDTO createBand(CreateBandRequest request);
}