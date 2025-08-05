package vn.utc.service.dtos;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class CustomerWithVehicleRequest {
    @NotNull
    @Valid
    private RegisterRequest registerRequest;

    @NotNull
    @Valid
    private VehicleDto vehicle;

    public CustomerWithVehicleRequest setRegisterRequest(RegisterRequest registerRequest) {
        this.registerRequest = registerRequest;
        return this;
    }

    public CustomerWithVehicleRequest setVehicle(VehicleDto vehicle) {
        this.vehicle = vehicle;
        return this;
    }
}