package com.emergency.web.dto.request.validator.user;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.emergency.web.dto.request.user.ModRequestDto;

/**
 * 
* @packageName     : com.emergency.web.dto.request.validator.user
* @fileName        : ModRequestValidator.java
* @author          : KHK
* @date            : 2025.08.19
* @description     : user 수정 요청 커스텀 밸리데이터 객체
* ===========================================================
* DATE              AUTHOR             NOTE
* -----------------------------------------------------------
* 2024.08.19        KHK                최초 생성
 */

@Component
public class ModRequestValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return ModRequestDto.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        ModRequestDto dto = (ModRequestDto) target;

        // changePassword 가 true일 때만 비밀번호 검증
        if (dto.isChangePassword()) {
            if (dto.getPassword() == null || dto.getPassword().isBlank()) {
                errors.rejectValue("password", "password.empty", "비밀번호는 필수 입력 사항입니다.");
            }
        }

        // 이메일 검증
        if (dto.getEmail() == null || dto.getEmail().isBlank()) {
            errors.rejectValue("email", "email.empty", "이메일은 필수 입력 사항입니다.");
        } else if (!dto.getEmail().matches("^(?:\\w+\\.?)*\\w+@(?:\\w+\\.)+\\w+$")) {
            errors.rejectValue("email", "email.invalid", "이메일 형식이 올바르지 않습니다.");
        }

        // 휴대폰 번호 검증
        if (dto.getHp() == null || dto.getHp().isBlank()) {
            errors.rejectValue("hp", "hp.empty", "휴대폰 번호는 필수 입력 사항입니다.");
        } else if (!dto.getHp().matches("^(010|011|016|017|018|019)-?\\d{3,4}-?\\d{4}$")) {
            errors.rejectValue("hp", "hp.invalid", "휴대폰 번호 형식이 올바르지 않습니다.");
        }

        // 우편번호
        if (dto.getPostCd() == null || dto.getPostCd().isBlank()) {
            errors.rejectValue("postCd", "postCd.empty", "우편 번호는 필수 입력 사항입니다.");
        }

        // 주소
        if (dto.getAddress() == null || dto.getAddress().isBlank()) {
            errors.rejectValue("address", "address.empty", "주소는 필수 입력 사항입니다.");
        }
    }
}
