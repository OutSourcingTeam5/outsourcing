package com.example.outsourcing.domain.store.service;


import com.example.outsourcing.domain.store.dto.request.StoreRequestDto;
import com.example.outsourcing.domain.store.dto.response.StoreSaveResponseDto;
import com.example.outsourcing.domain.store.repository.StoreRepository;
import com.example.outsourcing.domain.user.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StoreService {

    private final StoreRepository storeRepository;
    private final UserRepository userRepository;

    public StoreSaveResponseDto saveStore(Long id, @Valid StoreRequestDto dto) {

//        User user = userRepository.findById(id)
//                                    .orElseThrow(() -> new StoreException(StoreErrorCode.USER_NOT_FOUND));
//
//        if(!Role.OWNER.equals(user.getRole())) {
//            throw new StoreException(StoreErrorCode.STORE_UNAUTHORIZED);
//        }
//
//        Store store = new Store(dto.getName(),dto.getOpenTime(),dto.getCloseTime(),dto.getMinOrderPrice(), StoreStatus.OPEN, dto.getCategory(), user);
//
//        storeRepository.save(store);

        return null;
    }
}
