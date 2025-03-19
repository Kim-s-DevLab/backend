package eightplusone.bit.fit.domain.user.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QUser is a Querydsl query type for User
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUser extends EntityPathBase<User> {

    private static final long serialVersionUID = -233296306L;

    public static final QUser user = new QUser("user");

    public final eightplusone.bit.fit.global.base.QBaseTimeEntity _super = new eightplusone.bit.fit.global.base.QBaseTimeEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDate = _super.createdDate;

    public final StringPath email = createString("email");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath image = createString("image");

    public final StringPath job = createString("job");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedDate = _super.modifiedDate;

    public final ListPath<eightplusone.bit.fit.domain.interest.entity.MyInterest, eightplusone.bit.fit.domain.interest.entity.QMyInterest> myInterests = this.<eightplusone.bit.fit.domain.interest.entity.MyInterest, eightplusone.bit.fit.domain.interest.entity.QMyInterest>createList("myInterests", eightplusone.bit.fit.domain.interest.entity.MyInterest.class, eightplusone.bit.fit.domain.interest.entity.QMyInterest.class, PathInits.DIRECT2);

    public final ListPath<eightplusone.bit.fit.domain.mysession.entity.MySession, eightplusone.bit.fit.domain.mysession.entity.QMySession> mySessions = this.<eightplusone.bit.fit.domain.mysession.entity.MySession, eightplusone.bit.fit.domain.mysession.entity.QMySession>createList("mySessions", eightplusone.bit.fit.domain.mysession.entity.MySession.class, eightplusone.bit.fit.domain.mysession.entity.QMySession.class, PathInits.DIRECT2);

    public final StringPath name = createString("name");

    public final StringPath provider = createString("provider");

    public final EnumPath<eightplusone.bit.fit.domain.auth.enums.Role> role = createEnum("role", eightplusone.bit.fit.domain.auth.enums.Role.class);

    public final NumberPath<Integer> years = createNumber("years", Integer.class);

    public QUser(String variable) {
        super(User.class, forVariable(variable));
    }

    public QUser(Path<? extends User> path) {
        super(path.getType(), path.getMetadata());
    }

    public QUser(PathMetadata metadata) {
        super(User.class, metadata);
    }

}

