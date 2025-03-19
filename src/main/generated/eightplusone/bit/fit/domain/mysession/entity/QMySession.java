package eightplusone.bit.fit.domain.mysession.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMySession is a Querydsl query type for MySession
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMySession extends EntityPathBase<MySession> {

    private static final long serialVersionUID = -1095059458L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QMySession mySession = new QMySession("mySession");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final eightplusone.bit.fit.domain.session.entity.QSession session;

    public final EnumPath<eightplusone.bit.fit.domain.mysession.enums.MySessionType> type = createEnum("type", eightplusone.bit.fit.domain.mysession.enums.MySessionType.class);

    public final eightplusone.bit.fit.domain.user.entity.QUser user;

    public QMySession(String variable) {
        this(MySession.class, forVariable(variable), INITS);
    }

    public QMySession(Path<? extends MySession> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QMySession(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QMySession(PathMetadata metadata, PathInits inits) {
        this(MySession.class, metadata, inits);
    }

    public QMySession(Class<? extends MySession> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.session = inits.isInitialized("session") ? new eightplusone.bit.fit.domain.session.entity.QSession(forProperty("session")) : null;
        this.user = inits.isInitialized("user") ? new eightplusone.bit.fit.domain.user.entity.QUser(forProperty("user")) : null;
    }

}

