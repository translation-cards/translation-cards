package org.mercycorps.translationcards;

import org.mercycorps.translationcards.media.DecoratedMediaManager;

import dagger.Component;

@Component(modules = {MockMediaModule.class}, dependencies = ApplicationComponent.class)
public interface TestBaseComponent extends BaseComponent {
    DecoratedMediaManager getDecoratedMediaManager();
}
