package org.wildflowergardening.backend.core.wildflowergardening.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.wildflowergardening.backend.core.kernel.application.exception.ApplicationLogicException;
import org.wildflowergardening.backend.core.wildflowergardening.domain.ShelterAccount;
import org.wildflowergardening.backend.core.wildflowergardening.domain.ShelterAccountRepository;
import org.wildflowergardening.backend.core.wildflowergardening.domain.auth.UserRole;

import java.util.Optional;

import static org.wildflowergardening.backend.core.kernel.application.exception.WildflowerExceptionType.*;

@RequiredArgsConstructor
@Service
public class ShelterAccountService {

    private final ShelterAccountRepository shelterAccountRepository;

    @Transactional
    public Long save(ShelterAccount shelterAccount) {
        boolean isAlreadyExist = shelterAccountRepository.findByEmailOrPhoneNumberAndDeletedAtIsNull(shelterAccount.getEmail(), shelterAccount.getPhoneNumber()).isPresent();
        if (isAlreadyExist) {
            throw new ApplicationLogicException(SHELTER_ACCOUNT_EMAIL_OR_PHONENUMBER_ALREADY_EXISTS);
        }

        return shelterAccountRepository.save(shelterAccount).getId();
    }

    @Transactional(readOnly = true)
    public ShelterAccount getShelterAccountByEmail(String email) {
        return shelterAccountRepository.findShelterAccountByEmail(email).orElseThrow(() -> new ApplicationLogicException(SHELTER_ACCOUNT_NOT_EXISTS));
    }

    @Transactional
    public void changePassword(Long id, String newPwEncrypted) {
//        shelterAccountRepository.findShelterAccountByEmail(email).ifPresent(shelterAccount -> shelterAccount.setPassword(newPwEncrypted));
        shelterAccountRepository.findById(id).ifPresent(shelterAccount -> shelterAccount.setPassword(newPwEncrypted));
    }

    @Transactional
    public void changeRole(Long accountId, UserRole role) {
        shelterAccountRepository.findById(accountId).ifPresent(shelterAccount -> shelterAccount.setUserRole(role));
    }

    @Transactional
    public Long deleteShelterAccount(Long shelterId, Long shelterAccountId) {
        Optional<ShelterAccount> shelterAccount = shelterAccountRepository.findShelterAccountByShelterIdAndId(shelterId, shelterAccountId);
        if (shelterAccount.isEmpty()) {
            throw new IllegalArgumentException("id = " + shelterAccountId + "인 관리자가 없습니다.");
        }

        shelterAccount.get().toSoftDeleted();
        return shelterAccount.get().getId();
    }

}
