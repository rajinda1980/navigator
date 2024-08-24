package org.phinxt.navigator.service;

import org.phinxt.navigator.dto.HooverRequest;
import org.phinxt.navigator.dto.HooverResponse;

public interface HooverService {
    HooverResponse cleanRoom(HooverRequest hooverRequest);
}
