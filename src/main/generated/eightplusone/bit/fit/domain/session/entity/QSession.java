package eightplusone.bit.fit.domain.session.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QSession is a Querydsl query type for Session
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QSession extends EntityPathBase<Session> {

    private static final long serialVersionUID = 773241438L;

    public static final QSession session = new QSession("session");

    public final NumberPath<Integer> audioChannel = createNumber("audioChannel", Integer.class);

    public final DateTimePath<java.time.LocalDateTime> endTime = createDateTime("endTime", java.time.LocalDateTime.class);

    public final NumberPath<Long> lectureDuration = createNumber("lectureDuration", Long.class);

    public final ListPath<eightplusone.bit.fit.domain.mysession.entity.MySession, eightplusone.bit.fit.domain.mysession.entity.QMySession> mySessions = this.<eightplusone.bit.fit.domain.mysession.entity.MySession, eightplusone.bit.fit.domain.mysession.entity.QMySession>createList("mySessions", eightplusone.bit.fit.domain.mysession.entity.MySession.class, eightplusone.bit.fit.domain.mysession.entity.QMySession.class, PathInits.DIRECT2);

    public final NumberPath<Long> sessionId = createNumber("sessionId", Long.class);

    public final StringPath sessionImage = createString("sessionImage");

    public final NumberPath<Integer> standardCount = createNumber("standardCount", Integer.class);

    public final DateTimePath<java.time.LocalDateTime> startTime = createDateTime("startTime", java.time.LocalDateTime.class);

    public final StringPath summary = createString("summary");

    public final StringPath title = createString("title");

    public QSession(String variable) {
        super(Session.class, forVariable(variable));
    }

    public QSession(Path<? extends Session> path) {
        super(path.getType(), path.getMetadata());
    }

    public QSession(PathMetadata metadata) {
        super(Session.class, metadata);
    }

}

