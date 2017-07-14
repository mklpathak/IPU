// Generated code from Butter Knife. Do not modify!
package com.jesushghar.uss.activities;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class StartupActivity$$ViewBinder<T extends com.jesushghar.uss.activities.StartupActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131689638, "field 'startImage'");
    target.startImage = finder.castView(view, 2131689638, "field 'startImage'");
  }

  @Override public void unbind(T target) {
    target.startImage = null;
  }
}
