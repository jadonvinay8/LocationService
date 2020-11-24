package com.capgemini.LocationService.utilities;

import com.capgemini.LocationService.dto.ErrorResponse;
import com.capgemini.LocationService.dto.MicroserviceResponse;

public class ResponseBuilder {

    public static MicroserviceResponse build(int status, Object response, ErrorResponse errorResponse) {
        if (status >= 200 && status <= 204)
            return new MicroserviceResponse(status, new MicroserviceResponse.Payload(response, null));
        else
            return new MicroserviceResponse(status, new MicroserviceResponse.Payload(null, errorResponse));
    }

}
