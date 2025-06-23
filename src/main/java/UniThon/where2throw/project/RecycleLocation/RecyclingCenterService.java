package UniThon.where2throw.project.RecycleLocation;

import UniThon.where2throw.project.RecycleLocation.DTO.RecyclingCenterDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecyclingCenterService {
    private final RecycleLocationRepository locationRepo;

    public List<RecyclingCenterDto> findNearby(double lat, double lng, double radiusKm) {
        return locationRepo.findNearby(lat, lng, radiusKm)
                .stream()
                .map(loc -> new RecyclingCenterDto(
                        loc.getName(),
                        loc.getAddress(),
                        loc.getLatitude(),
                        loc.getLongitude(),

                        loc.getWasteTypes()
                                .stream()
                                .map(e -> e.getWasteType())
                                .collect(Collectors.toList()),
                        loc.getSpecialWasteTypes()
                                .stream()
                                .map(e -> e.getSpecialWasteType())
                                .collect(Collectors.toList())
                ))
                .collect(Collectors.toList());
    }
}
