package com.aliens.hipster.domain;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDateTime;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import java.io.Serializable;

/**
 * A Client.
 */
@Entity
@Table(name = "client")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Client implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final DateTimeZone timeZone = DateTimeZone.forID("Asia/Kolkata");

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "name")
    private String name;


    private String state;

    @Column(name = "read_offset")
    private Integer readOffset;

    @Column(name = "page_size")
    private Integer pageSize;

    @Column(name = "uri")
    private String uri;

    //@Type(type = "org.jadira.usertype.dateandtime.joda.PersistentLocalDateTime")
    private String lastProcessedAt;


    public void updateProcessedTime() {
        lastProcessedAt = LocalDateTime.now(timeZone).toString();
    }

}
