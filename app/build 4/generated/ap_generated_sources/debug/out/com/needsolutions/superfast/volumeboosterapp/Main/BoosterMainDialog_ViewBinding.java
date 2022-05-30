// Generated code from Butter Knife. Do not modify!
package com.needsolutions.superfast.volumeboosterapp.Main;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.needsolutions.superfast.volumeboosterapp.R;
import eu.gsottbauer.equalizerview.EqualizerView;
import java.lang.IllegalStateException;
import java.lang.Override;

public class BoosterMainDialog_ViewBinding implements Unbinder {
  private BoosterMainDialog target;

  @UiThread
  public BoosterMainDialog_ViewBinding(BoosterMainDialog target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public BoosterMainDialog_ViewBinding(BoosterMainDialog target, View source) {
    this.target = target;

    target.layout_volume = Utils.findRequiredViewAsType(source, R.id.layout_volume, "field 'layout_volume'", LinearLayout.class);
    target.tv_volume = Utils.findRequiredViewAsType(source, R.id.tv_volume, "field 'tv_volume'", TextView.class);
    target.tv_boost = Utils.findRequiredViewAsType(source, R.id.tv_boost, "field 'tv_boost'", TextView.class);
    target.equalizer_view = Utils.findRequiredViewAsType(source, R.id.equalizer_view, "field 'equalizer_view'", EqualizerView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    BoosterMainDialog target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.layout_volume = null;
    target.tv_volume = null;
    target.tv_boost = null;
    target.equalizer_view = null;
  }
}
