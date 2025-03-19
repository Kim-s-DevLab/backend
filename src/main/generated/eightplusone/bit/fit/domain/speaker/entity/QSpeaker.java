package eightplusone.bit.fit.domain.speaker.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QSpeaker is a Querydsl query type for Speaker
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QSpeaker extends EntityPathBase<Speaker> {

    private static final long serialVersionUID = -1835721922L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QSpeaker speaker = new QSpeaker("speaker");

    public final StringPath description = createString("description");

    public final StringPath image = createString("image");

    public final StringPath name = createString("name");

    public final eightplusone.bit.fit.domain.session.entity.QSession session;

    public final NumberPath<Long> speakerId = createNumber("speakerId", Long.class);

    public QSpeaker(String variable) {
        this(Speaker.class, forVariable(variable), INITS);
    }

    public QSpeaker(Path<? extends Speaker> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QSpeaker(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QSpeaker(PathMetadata metadata, PathInits inits) {
        this(Speaker.class, metadata, inits);
    }

    public QSpeaker(Class<? extends Speaker> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.session = inits.isInitialized("session") ? new eightplusone.bit.fit.domain.session.entity.QSession(forProperty("session")) : null;
    }

}

