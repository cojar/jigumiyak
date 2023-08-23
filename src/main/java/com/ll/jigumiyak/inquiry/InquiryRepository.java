package com.ll.jigumiyak.inquiry;

import com.ll.jigumiyak.user.SiteUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InquiryRepository extends JpaRepository<Inquiry, Long> {

    Page<Inquiry> findAllByInquirer(SiteUser inquirer, Pageable pageable);
    List<Inquiry> findAllByInquirer(SiteUser inquirer);
}
