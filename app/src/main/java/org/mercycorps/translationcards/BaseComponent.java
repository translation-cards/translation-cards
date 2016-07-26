package org.mercycorps.translationcards;

import org.mercycorps.translationcards.media.DecoratedMediaManager;

import dagger.Component;

@Component(modules = {MediaModule.class}, dependencies = ApplicationComponent.class)
public interface BaseComponent {
    DecoratedMediaManager getDecoratedMediaManager();
}
