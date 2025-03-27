package com.mickey.dinggading.domain.band.service;

import com.mickey.dinggading.domain.band.model.entity.Band;
import com.mickey.dinggading.model.*;

import java.util.UUID;

public interface BandService {

    // 밴드 관련 기능
    Band createBand(CreateBandRequest request, UUID memberId);
}