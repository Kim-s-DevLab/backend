package eightplusone.bit.fit.domain.interest.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMyInterest is a Querydsl query type for MyInterest
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMyInterest extends EntityPathBase<MyInterest> {

    private static final long serialVersionUID = 1852718424L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QMyInterest myInterest = new QMyInterest("myInterest");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final QInterest interest;

    public final eightplusone.bit.fit.domain.user.entity.QUser user;

    public QMyInterest(String variable) {
        this(MyInterest.class, forVariable(variable), INITS);
    }

    public QMyInterest(Path<? extends MyInterest> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QMyInterest(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QMyInterest(PathMetadata metadata, PathInits inits) {
        this(MyInterest.class, metadata, inits);
    }

    public QMyInterest(Class<? extends MyInterest> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.interest = inits.isInitialized("interest") ? new QInterest(forProperty("interest")) : null;
        this.user = inits.isInitialized("user") ? new eightplusone.bit.fit.domain.user.entity.QUser(forProperty("user")) : null;
    }

}

