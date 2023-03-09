package org.stanislav.forms;

/**
 * @author Stanislav Hlova
 */
public interface Form<T> {
    /**
     * Return a T class, which represents, for example, a model
     *
     * @return a T class.
     */
    T getModel();
}
