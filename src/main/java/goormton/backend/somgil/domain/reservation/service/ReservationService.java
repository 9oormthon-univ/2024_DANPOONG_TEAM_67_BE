package goormton.backend.somgil.domain.reservation.service;

import goormton.backend.somgil.domain.option.domain.Options;
import goormton.backend.somgil.domain.packages.domain.Packages;
import goormton.backend.somgil.domain.packages.domain.repository.PackagesRepository;
import goormton.backend.somgil.domain.reservation.domain.Reservation;
import goormton.backend.somgil.domain.reservation.domain.repository.ReservationRepository;
import goormton.backend.somgil.domain.reservation.dto.request.DriverReservationRequest;
import goormton.backend.somgil.domain.reservation.dto.request.ReservationRequest;
import goormton.backend.somgil.domain.reservation.dto.response.DriverReservationResponse;
import goormton.backend.somgil.domain.reservation.dto.response.ReservationResponse;
import goormton.backend.somgil.domain.user.domain.User;
import goormton.backend.somgil.domain.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;
    private final PackagesRepository packagesRepository;

    @Transactional
    public ReservationResponse createReservation(ReservationRequest request, User loggedUser) {
        //User loggedUser = getCurrentUser();

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
                .startDate(getParsedDate(request.getStartDate()))
                .endDate(getParsedDate(request.getEndDate()))
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
                .packageId(packages.getPackageId())
                .userName(request.getUserName())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .selectedOptions(request.getSelectedOptions())
                .adultCount(request.getAdultCount())
                .childCount(request.getChildCount())
                .infantCount(request.getInfantCount())
                .totalPrice(totalPrice)
                .build();
    }

    @Transactional
    public DriverReservationResponse createDriverReservation(DriverReservationRequest request) {
        User loggedUser = getCurrentUser();

        // 사용 가능한 기사 검색
        User availableDriver = userRepository.findFirstAvailableDriver()
                .orElseThrow(() -> new IllegalArgumentException("사용 가능한 기사가 없습니다."));

        // 예약 총 가격 계산
        int totalPrice = calculatePrice(request);

        // 예약 생성
        Reservation reservation = Reservation.builder()
                .user(loggedUser)
                .driver(availableDriver)
                .pickupLocation(request.getPickupLocation())
                .dropOffLocation(request.getDropOffLocation())
                .startDate(getParsedDate(request.getDate()))
                .time(request.getTime())
                .adultCount(request.getAdultCount())
                .childCount(request.getChildCount())
                .infantCount(request.getInfantCount())
                .totalPrice(totalPrice)
                .driver(availableDriver)
                .pickupLocation(request.getPickupLocation())
                .dropOffLocation(request.getDropOffLocation())
                .build();

        reservationRepository.save(reservation);

        // 기사 상태 업데이트
        availableDriver.setAvailable(false);
        userRepository.save(availableDriver);

        return DriverReservationResponse.builder()
                .driverName(availableDriver.getNickname())
                .contact(availableDriver.getEmail()) // 이메일을 연락처로 가정
                .pickupLocation(request.getPickupLocation())
                .dropOffLocation(request.getDropOffLocation())
                .date(request.getDate())
                .time(request.getTime())
                .totalPrice(totalPrice)
                .build();
    }

    @Transactional
    public List<ReservationResponse> findAllByUser() {
        User loggedUser = getCurrentUser();

        // 사용자의 예약 조회
        List<Reservation> reservations = reservationRepository.findReservationsByUser(loggedUser.getId());

        // Reservation을 ReservationResponse로 변환
        return reservations.stream()
                .map(reservation -> ReservationResponse.builder()
                        .packageName(reservation.getPackages().getName())
                        .startDate(reservation.getStartDate().toString())
                        .endDate(reservation.getEndDate().toString())
                        .selectedOptions(reservation.getSelectedOptions().stream()
                                .map(Options::getContent)
                                .collect(Collectors.toList()))
                        .adultCount(reservation.getAdultCount())
                        .childCount(reservation.getChildCount())
                        .infantCount(reservation.getInfantCount())
                        .totalPrice(reservation.getTotalPrice())
                        .pickupLocation(reservation.getPickupLocation())
                        .dropOffLocation(reservation.getDropOffLocation())
                        .status(reservation.getStatus())
                        .build())
                .collect(Collectors.toList());
    }

    @Transactional
    public void cancelledReservation(String reservationId) {
        Reservation reservation = reservationRepository.findByReservationId(reservationId);
        reservation.setStatus("CANCELLED");
        reservationRepository.save(reservation);
    }

    private int calculatePrice(DriverReservationRequest request) {
        // 기본 요금
        int baseFare = 5000;

        // 성인, 아동, 유아의 개별 요금 비율
        int adultFare = 10000;  // 성인 1명당 요금
        int childFare = 7000;   // 아동 1명당 요금
        int infantFare = 3000;  // 유아 1명당 요금

        // 총 요금 계산
        return baseFare
                + (request.getAdultCount() * adultFare)
                + (request.getChildCount() * childFare)
                + (request.getInfantCount() * infantFare);
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

    public LocalDate getParsedDate(String date) {
        try {
            return LocalDate.parse(date); // 문자열을 LocalDate로 변환
        } catch (DateTimeParseException e) {
            throw new IllegalStateException("날짜 형식이 올바르지 않습니다.");
        }
    }
}
