package sk.liptovzije.service.impl;

import com.mysema.query.jpa.hibernate.HibernateQuery;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sk.liptovzije.model.DO.EventDO;
import sk.liptovzije.model.DO.QEventDO;
import sk.liptovzije.service.IEventService;

import javax.transaction.Transactional;

@Service
@Transactional
public class EventService implements IEventService {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public EventDO getById(long id) {
        HibernateQuery query = new HibernateQuery(sessionFactory.getCurrentSession());

        QEventDO event = QEventDO.eventDO;
        EventDO result = query.from(event)
                .where(event.id.eq(id))
                .uniqueResult(event);

        return result;
    }

    @Override
    public Long saveEvent(EventDO user) {
        try {
            sessionFactory.getCurrentSession().save(user);
        }  catch (Exception e) {
            e.printStackTrace();
        }

        return user.getId();
    }
}
