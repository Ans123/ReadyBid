package net.readybid.app.entities.core;

import net.readybid.entities.Id;

public interface ActionReportBuilder<T> {

    ActionReport<T> build(Id bidId, T tObject);
}
