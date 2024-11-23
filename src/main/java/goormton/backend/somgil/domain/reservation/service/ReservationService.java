package goormton.backend.somgil.domain.reservation.service;

import goormton.backend.somgil.domain.option.domain.Options;
import goormton.backend.somgil.domain.packages.domain.Packages;
import goormton.backend.somgil.domain.packages.domain.repository.PackagesRepository;
import goormton.backend.somgil.domain.reservation.domain.Reservation;
import goormton.backend.somgil.domain.reservation.domain.repository.ReservationRepository;
import goormton.backend.somgil.domain.reservation.dto.request.ReservationRequest;
import goormton.backend.somgil.domain.reservation.dto.response.ReservationResponse;
import goormton.backend.somgil.domain.user.domain.User;
import goormton.backend.somgil.domain.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;
    private final PackagesRepository packagesRepository;

    @Transactional
    public ReservationResponse createReservation(ReservationRequest request) {
        User loggedUser = getCurrentUser();

        // 패키지 조회
        Packages packages = packagesRepository.findByPackageId(request.getPackageId())
                .orElseThrow(() -> new IllegalArgumentException("해당 패키지를 찾을 수 없습니다."));

        // 총 가격 계산
        int totalPrice = (request.getAdultCount() * packages.getAdultPrice()) +
                (request.getChildCount() * packages.getChildPrice()) +
                (request.getInfantCount() * packages.getInfantPrice());

        // 옵션 리스트 변환
        List<Options> optionsList = request.getSelectedOptions().stream()
                .map(optionContent -> Options.builder()
                        .content(optionContent)
                        .checked(true) // 선택된 옵션은 항상 true
                        .build())
                .collect(Collectors.toList());

        // 예약 생성
        Reservation reservation = Reservation.builder()
                .user(loggedUser)
                .packages(packages)
                .date(request.getDate())
                .selectedOptions(optionsList)
                .adultCount(request.getAdultCount())
                .childCount(request.getChildCount())
                .infantCount(request.getInfantCount())
                .totalPrice(totalPrice)
                .build();

        reservationRepository.save(reservation);

        // 응답 반환
        return ReservationResponse.builder()
                .packageName(packages.getName())
                .userName(request.getUserName())
                .date(request.getDate())
                .selectedOptions(request.getSelectedOptions())
                .adultCount(request.getAdultCount())
                .childCount(request.getChildCount())
                .infantCount(request.getInfantCount())
                .totalPrice(totalPrice)
                .build();
    }

    private User getCurrentUser() {
        // 현재 로그인된 사용자 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalStateException("현재 인증된 사용자가 없습니다.");
        }

        User currentUser = (User) authentication.getPrincipal();
        return userRepository.findById(currentUser.getId())
                .orElseThrow(() -> new IllegalStateException("사용자를 찾을 수 없습니다."));
    }
}
