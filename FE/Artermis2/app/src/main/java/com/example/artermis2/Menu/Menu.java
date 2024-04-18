package com.example.artermis2.Menu;

import android.content.Context;
import android.widget.FrameLayout;

public abstract class Menu {
    FrameLayout main = null;
    Context context;
    public abstract void onClick();
    public abstract FrameLayout getMain();
}
