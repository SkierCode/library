package ru.itstudy.library.jsfui.converter;

import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.itstudy.library.dao.PublisherDao;
import ru.itstudy.library.domain.Author;
import ru.itstudy.library.domain.Publisher;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

@FacesConverter(forClass = Publisher.class)
@Component
public class PublisherConverter implements Converter {

    @Autowired
    PublisherDao publisherDao;

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        if (Strings.isNullOrEmpty(value)) {
            return null;
        }
        return publisherDao.get(Integer.parseInt(value));
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        if (value == null) {
            return null;
        }
        return ((Publisher)value).getId().toString();
    }
}
