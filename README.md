[TOC]

***由于采用 Typora 书写，github 不支持部分格式，建议 clone 后用 Typora 观看***

# 自定义View与动画

## 一、自定义View

### 1、为什么要自定义？

> 一句话：官方的控件满足不了产品想要的效果 (
>
> 但其实，官方控件已经能满足绝大多数需求了，正常情况下自定义 View 是下下策，因为造自定义 View 的轮子是一件很耗时也很耗精力的事。
>
> 这里分享一下我大一暑假时遇到的事情，当时共 5 个组分开做积分商城，产品给了一个积分商城的设计图，其中主页有一个上滑展开的需求，我们组（我和钟智），我就负责做这个上滑的界面。当时我花了好像 3、4 天的时间去设计一个自定义 View（原谅我当时还不知道协调者布局这个东西），结果啊，产品改需求了，靠
>
> 经过上面的这个故事后，相信大家应该理解到了选择自定义 View 会面临的风险
>
> 后来我又花了接近一周的时间还是用自定义 View 写出了产品的新需求，限于当时我的能力，现在去看当时写的代码，存在一些耦合的地方，算是给以后学弟留坑了😁
>
> OK，回到这里，自定义 View 确实是下下策，不止会影响你的心情，如果你的代码设计得不好的话（比如耦合度很高，代码逻辑混乱），还会影响以后维护代码人的心情。（说个笑话：听说代码写得越烂，越不容易被辞退 :）

### 2、分类

> 这里自定义 View 是一个习惯性的统称
>
> 如果按照类型来分：
>
> - 自定义 View
>
>   > 指直接或间接继承于 View 的控件，如：TextView、Button
>   >
>   > ```java
>   > public class TextView extends View {}
>   > ```
>   >
>   > ```java
>   > public class Button extends TextView {}
>   > ```
>   >
>   > 
>
> - 自定义 ViewGroup
>
>   > ```java
>   > public class LinearLayout extends ViewGroup {}
>   > ```
>   >
>   > ```java
>   > public class FrameLayout extends ViewGroup {}
>   > ```
>
> 
>
> 如果按照学习的内容来分，大致分为以下内容
>
> - View 的测量与布局
>
>   > 测量就是与 Measure 相关的方法，如：`onMeasure()`
>   >
>   > 布局就是与 Layout 相关的方法，如：`onLayout()`
>   >
>   > 这两个一般是在一起调用的，并且通常是在 自定义 ViewGroup 中才会涉及到（View 中也可以实现，比如 TextView 就重写了 `onLayout()` 方法）
>
> - View 的绘制
>
>   > 绘制就是与 Draw 相关的方法，如：`onDraw()`
>   >
>   > 为什么要列出绘制？
>   >
>   > 因为很多动画都是依靠重写绘制来实现的，所以绘制也比较重要
>
> - 事件分发
>
>   >一般指触摸事件的分发
>   >
>   >这个应该是自定义 View 中最难也最重要的内容了，如果你想设计出一些很炫酷的界面，就得精通事件分发
>   >
>   >事件分发会在下下节课讲解
>
> 接下来就是正式开始讲解自定义 View 的时间，由于时间原因，在加上你们的现阶段能力的原因，本节课更偏向于讲解如何使用自定义 View，而不是讲解底层内容，底层内容以博客的形式分享

### 3、构造函数

> 该部分内容参考：https://www.cnblogs.com/angeldevil/p/3479431.html#three
>
> View 一共有四个构造函数
>
> ```java
> // 这个用于在代码中直接 new 一个 View
> public View(Context context)
>     
> // 这个用于在 xml 中书写，系统自动将你写在 xml 中的属性装换为 AttributeSet 类，然后调用这个函数
> public View(
>     Context context, 
>     @Nullable AttributeSet attrs
> )
> 
> // 这个一般用于设置默认属性，使用的情况较少
> // 系统不会主动调用，一般是开发者自己设置
> public View(
>     Context context, 
>     @Nullable AttributeSet attrs, 
>     int defStyleAttr
> )
> 
> // 这个与上面的类似，也是设置默认属性，使用的情况很少，且只支持 Android 5.0 以上
> // 系统不会主动调用
> public View(
>     Context context, 
>     @Nullable AttributeSet attrs, 
>     int defStyleAttr, 
>     int defStyleRes
> )
> ```
>
> 前面两个构造函数各位应该都能看懂，关键在于后面两个，他们与设置默认属性相关，在讲解前我们先得知道一个 View 可以通过哪些方式设置属性
>
> ### 1、直接写在 XML 中
>
> > ```xml
> > <androidx.cardview.widget.CardView
> >     android:layout_width="200dp"
> >     android:layout_height="200dp"
> >     app:cardBackgroundColor="@android:color/darker_gray"
> >     app:cardCornerRadius="8dp"/>
> > ```
> >
> > 这样就会给当前 View 设置属性
> >
> > 原理是系统自动把这些属性写入到 AttributeSet 类里面，然后调用
> >
> > ```java
> > public View(Context context, @Nullable AttributeSet attrs)
> > ```
> >
> > 来生成 View 对象
>
> ### 2、使用 @style 设置属性
>
> > 在 `style.xml` 中：
> >
> > ```xml
> > <style name="myCardView_style">
> >     <item name="cardBackgroundColor">@android:color/darker_gray</item>
> >     <item name="cardCornerRadius">8dp</item>
> > </style>
> > ```
> >
> > 在 `layout.xml` 中：
> >
> > ```xml
> > <androidx.cardview.widget.CardView
> >     android:layout_width="200dp"
> >     android:layout_height="200dp"
> >     style="@style/myCardView_style"/>
> > ```
> >
> > 这个一般用于多个相同控件复用属性的时候
> >
> > 原理与上一个是一样的，它会把 `style="@style/MyCardView"` 里写的属性一起写入 AttributeSet 类里面
> >
> > > 问题：如果 `style="@style/MyCardView"` 与 xml 中有相同属性它会怎么处理呢？
> > >
> > > 经过测试后，xml 中定义的属性会覆盖 `style="@style/MyCardView"` 中定义的属性
> >
> > 
>
> ### 3、在 theme 中设置某种控件的默认属性
>
> > 在 `theme.xml` 中：
> >
> > ```xml
> > <style name="MyAppTheme" parent="Theme.MaterialComponents">
> >     <!--下面这个 @style/MyCardView 就是前面写的 <style name="MyCardView">-->
> >     <item name="cardViewStyle">@style/myCardView_style</item>
> > </style>
> > ```
> >
> > > 这里 `name="cardViewStyle"` 意思是定义 CardView 的默认属性
> > > 这个默认属性是如何生效的呢？
> > >
> > > ```java
> > > // 原因在于 CardView 的构造函数
> > > public CardView(
> > >     @NonNull Context context, 
> > >     @Nullable AttributeSet attrs
> > > ) {
> > >     this(context, attrs, R.attr.cardViewStyle);
> > > }
> > > 
> > > public CardView(
> > >     @NonNull Context context, 
> > >     @Nullable AttributeSet attrs, 
> > >     int defStyleAttr
> > > ) {
> > >     super(context, attrs, defStyleAttr);
> > > }
> > > ```
> > >
> > > 这里我们可以发现，两个参数的构造函数使用了 `this(context, attrs, R.attr.cardViewStyle)` 调用了三个参数的构造函数，其中他给第三个参数传入了一个值 `R.attr.cardViewStyle`，我们去扒它源码，看下这是什么东西（直接点击导包中的 `import androidx.cardview.R` 就可以跳到源码中）
> > >
> > > ```xml
> > > <resources>
> > >     <attr format="reference" name="cardViewStyle"/>
> > > </resources>
> > > ```
> > >
> > > 这里可能你们会看不懂，我简单讲一下：这个 `format="reference"` 代表这个属性接受的类型，而 `reference` 表示接受的类型为一个引用值，比如：`@style/xxx`、`@color/xxx`、`@drawable/xxx` 就是引用值
> > >
> > > 然后 `name="cardViewStyle"` 代表这个属性的唯一名字
> > >
> > > 这个 attr 的引用就是 CardView 可以在 theme 中设置默认属性的原因，因为系统调用 CardView 两个参数的构造函数，然后它这个构造函数主动调用了三个参数的构造函数，并且传入了 `R.attr.cardViewStyle`，所以构造函数的第三个参数对应的是一个`style` 格式的引用，但需要你自己在 View 中指定名字
> >
> > 引用 `theme.xml` 有以下几种方式：
> >
> > > - 全局引用，所有 View 都会使用该配置
> > >
> > >   ```xml
> > >   <application
> > >       android:theme="@style/MyAppTheme">
> > >   </application>
> > >   ```
> > >
> > > - 某个 Activity 引用，当前 ACtivity 内使用该配置
> > >
> > >   ```xml
> > >   <activity
> > >       android:name=".section3.test.TestActivity"
> > >       android:theme="@style/MyAppTheme"
> > >       android:exported="false" />
> > >   ```
> > >
> > > - 某个 Activity 使用代码引用
> > >
> > >    ```kotlin
> > >    override fun onCreate(savedInstanceState: Bundle?) {
> > >        super.onCreate(savedInstanceState)
> > >        setTheme(R.style.MyAppTheme)
> > >        setContentView(R.layout.layout_card0)
> > >    }
> > >    // 注意在 Activity 中使用必须在 setContentView 之前
> > >    ```
> > >
> > > - 单个 View 使用
> > >
> > >   ```xml
> > >   <androidx.cardview.widget.CardView
> > >       android:layout_width="200dp"
> > >       android:layout_height="200dp"
> > >       android:theme="@style/MyAppTheme" />
> > >   ```
> >
> > 看到这里你可能有点晕，我们简单梳理一下流程：
> >
> > ```flow
> > read=>operation: 系统读取你写的 xml
> > new=>operation: 生成 AttributeSet，然后调用两个参数的构造函数
> > first=>operation: 两个参数的调用三个参数的，并传入 R.attr.cardViewStyle
> > second=>operation: View 开始从 theme 中读取属性
> > third=>operation: View 在 theme 中发现 <item name="cardViewStyle">@style/myCardView_style</item> 的定义
> > fourth=>operation: 读取 @style/MyCardView 里面的属性
> > read->new->first->second->third->fourth
> > ```
> >
> > 
>
> ### 4、调用构造函数的 defStyleRes
>
> > 由于 CardView 没有重写第四个构造函数（可能是为了兼容 Android 5.0 以下？），所以这里我用 FrameLayout 来演示
> >
> > 先在 `style.xml` 中定义属性：
> >
> > ```xml
> > <style name="myFrameLayout">
> >     <item name="android:layout_width">match_parent</item>
> >     <item name="android:layout_height">match_parent</item>
> >     <item name="android:background">@android:color/black</item>
> > </style>
> > ```
> >
> > 然后在代码中这样使用：
> >
> > ```kotlin
> > val frameLayout = FrameLayout(this, null, 0, R.style.myFrameLayout_style)
> > ```
> >
> > 当你不能在 xml 中申明，只能在代码中动态生成时，就可以使用这种写法，有个优点就是使用 `style.xml` 可以进行属性的复用，但一般都用不到复用，这时候就可以使用 LayoutParams 和调用对应方法来代替：
> >
> > ```kotlin
> > val frameLayout = FrameLayout(this)
> > // Android 第一节课我们讲过，父布局可以给子布局设置属性
> > // 且属性都以 layout_ 开头，所以 layout_width 和 layout_height 
> > // 对应由 LayoutParams 来设置
> > frameLayout.layoutParams = ViewGroup.LayoutParams(
> >     ViewGroup.LayoutParams.MATCH_PARENT,
> >     ViewGroup.LayoutParams.MATCH_PARENT
> > )
> > // background 属于 View 自身属性，所以对应 View 自身方法
> > frameLayout.setBackgroundColor(Color.BLACK)
> > 
> > // 但这样写有一个缺点就是：LayoutParams 这东西只能设置少量的属性，
> > // 而且 LayoutParams 有很多个，具体选择那个需要看你的 View 被添加到
> > // 哪种父布局里，如果是 LinearLayout，就是 LinearLayout.LayoutParams(...)
> > ```
> >
> > 可能有人不知道 LayoutParams 是什么东西，你暂时可以把它看成是用来保存 View 中有关**父布局属性**的一个数据类，供父布局使用，后面会再次讲解
> >
> > 除了上面这种动态生成时传入默认属性，你也可以在构造器中设置你的自定义 View 的默认属性：
> >
> > 一般是这样定义构造器的：
> >
> > ```java
> > // 由于 java 是基础，我先用 java 来定义构造器，后面再使用 kt 来优化写法
> > public class Section3View extends View {
> >     public Section3View(Context context) {
> >         // 注意这里调用的是 this，而不是 super
> >         this(context, null);
> >     }
> > 
> >     public Section3View(
> >         Context context, 
> >         @Nullable AttributeSet attrs
> >     ) {
> >         // 注意这里调用的是 this，而不是 super
> >         this(context, attrs, R.attr.mySection3View_attrs);
> >     }
> > 
> >     public Section3View(
> >         Context context, 
> >         @Nullable AttributeSet attrs, 
> >         int defStyleAttr
> >     ) {
> >         // 注意这里调用的是 this，而不是 super
> >         // 这里就是设置默认属性
> >         this(
> >             context, 
> >             attrs, 
> >             defStyleAttr, 
> >             R.style.mySection3View_defaultAttrs
> >         );
> >     }
> > 
> >     public Section3View(
> >         Context context, 
> >         @Nullable AttributeSet attrs, 
> >         int defStyleAttr, 
> >         int defStyleRes
> >     ) {
> >         // 直到这里才调用 super
> >         super(context, attrs, defStyleAttr, defStyleRes);
> >     }
> > }
> > ```
> >
> > 其中 `R.attr.mySection3View_attrs`：
> >
> > ```xml
> > <!--value/attrs.xml-->
> > <resources>
> >     <attr name="mySection3View_attrs" format="reference"/>
> > </resources>
> > ```
> >
> > 其中 `R.style.mySection3View_defaultAttrs`：
> >
> > ```xml
> > <!--value/style.xml-->
> > <style name="mySection3View_defaultAttrs">
> >     <item name="android:background">@android:color/black</item>
> > </style>
> > ```
> >
> > 如果你去对照官方控件的写法，你会发现他们的构造函数也是这样写的，前三个都是调用 `this()`，只有最后一个调用   `super()`，但他们一般只设置了 `R.attr.mySection3View_attrs`，用于开发者在 theme 中定义全局属性（[第三点](#3、在 theme 中设置某种控件的默认属性)），而 `R.style.mySection3View_defaultAttrs` 在官网控件中一般没设置，都是写个 0，代表没得默认属性
> >
> > >为什么有两个默认属性？
> > >
> > >```java
> > >public View(
> > >    Context context, 
> > >    @Nullable AttributeSet attrs, 
> > >    int defStyleAttr, 
> > >    int defStyleRes
> > >)
> > >```
> > >
> > >你会发现第三个参数 `defStyleAttr` 和第四个 `defStyleRes` 都是设置默认参数，但为什么要同时存在两个呢？
> > >
> > >原因：`defStyleAttr` 是从 theme 中读取默认值，但如果我们只能动态生成 View，又需要多次用到一些属性，在 Android 5.0 之前要么每次都手动调用方法来设置，要么写在 theme 中，但这样未免麻烦了些，在 Android 5.0 后增加了这个 `defStyleRes` 参数，就可以直接把属性写在 `style.xml` 中，方便复用
> > >
> > >**但这里有很重要的一点：**使用 `defStyleRes` 时不能设置 `defStyleAttr`，给 `defStyleAttr` 填入 0 或者 theme 中不设置对应的属性即可
> >
> > 回到这里，前面 java 的写法确实感觉有些臃肿了，我们来看看 kt 的写法：
> >
> > ```kotlin
> > // 必须添加 @JvmOverloads，不然 java 层无法使用该构造器
> > // 会导致 xml 中的 View 无法生成
> > class Section3ViewKt @JvmOverloads constructor(
> >     context: Context,
> >     attrs: AttributeSet? = null,
> >     defStyleAttr: Int = R.attr.mySection3View_attrs,
> >     defStyleRes: Int = R.style.mySection3View_defaultAttrs
> > ) : View(context, attrs, defStyleAttr, defStyleRes) {
> > }
> > // 如果你没得 R.attr.mySection3View 或 R.style.MySection3View_defaultAttrs
> > // 可以直接赋值为 0，就是没得默认属性设置
> > ```
>
> **讲到这里，构造函数中的四个参数各位应该知道有什么作用了，这里我们总结一下：**
>
> ```java
> public View(Context context)
> // 用于在代码中动态生成自定义 View
> ```
>
> ```java
> public View(
>     Context context, 
>     @Nullable AttributeSet attrs
> )
> // 用于写在 xml 里面系统调用
> ```
>
> ```java
> public View(
>     Context context,
>     @Nullable AttributeSet attrs, 
>     int defStyleAttr
> )
> // 用于设置全局默认属性值，通常是通过上面两个参数的构造函数来间接调用
> ```
>
> ```java
> public View(
>     Context context, 
>     @Nullable AttributeSet attrs, 
>     int defStyleAttr, 
>     int defStyleRes
> )
> // 也是用于设置默认属性值，只是不用写在 theme 中，通常用于动态生成代码中或自己自定义 View 的时候
> ```
>
> 前面讲了 4 种方式来设置属性，还有一种方式也可以来设置属性
>
> ### 5、直接在 theme 中定义属性
>
> 这个与第三点：[在 theme 中设置某种控件的默认属性](#3、在 theme 中设置某种控件的默认属性) 有些类似，但它值允许直接写在 theme 中
>
> ```xml
> <resources xmlns:tools="http://schemas.android.com/tools">
>     <style name="Theme.CustomViewWithAnimStudy" parent="Theme.MaterialComponents.DayNight.DarkActionBar">
>         <!--如果你加了这行代码，你会发现应用大部分控件变成黑色了-->
>         <item name="android:background">@android:color/black</item>
>     </style>
> </resources>
> ```
>
> **但是，**这个是不分控件的给全部 View 设置属性，一般不会使用到它，只有设置整个应用的主题时会用到
>
> 比如你可以试试把这几个属性改一下，就会发现应用的标题栏颜色从紫色改成了你设置的颜色
>
> ```xml
> <item name="colorPrimary">@color/purple_500</item>
> <item name="colorPrimaryVariant">@color/purple_700</item>
> <item name="colorOnPrimary">@color/white</item>
> <item name="colorSecondary">@color/teal_200</item>
> <item name="colorSecondaryVariant">@color/teal_700</item>
> <item name="colorOnSecondary">@color/black</item>
> ```
>
> 这些属性对应了某种具体位置的颜色，可以看看这篇博客：https://blog.csdn.net/smartzzg/article/details/104788412
>
> ### 五种属性定义方法的顺序
>
> > 我们讲了有 5 种不同的属性定义，如果定义了相同属性，他们存在一个优先级顺序：
> >
> > [1、直接写在 XML 中](#1、直接写在 XML 中)
> >
> > [2、使用 @style 设置属性](#2、使用 @style 设置属性)
> >
> > [3、在 theme 中设置某种控件的默认属性](#3、在 theme 中设置某种控件的默认属性)
> >
> > [4、调用构造函数的 defStyleRes](#4、调用构造函数的 defStyleRes)
> >
> > [5、直接在 theme 中定义属性](#5、直接在 theme 中定义属性)
> >
> > 这里注意：使用 `defStyleRes ` 时不能设置 `defStyleAttr`，给 `defStyleAttr` 填入 0 或者 theme 中不设置对应的属性即可
>
> **到这里 View 的四个构造函数基本分析完毕了，接下来我们开始讲解里面的方法**

### 4、onDraw()

> 

### 5、自定义属性AttributeSet

### 6、invalidate()

### 7、onMeasure()

### 8、onLayout()

### 9、LayoutParams

### 10、requestLayout()

### 11、*ViewRootImpl

### 12、发布开源库

### 13、分享一些东西

> 谁想写自定义 View 啊，你想写吗？正经人写什么自定义 View。
>
> 自定义 View 确实很难，东西又多又杂，在考虑写自定义 View 之前，请先搜索一下是否有别人已经造好的轮子，有轮子直接用，他不香吗？[狗头]
>
> 这里分享一些轮子：
>
> ### 1、Material Design 官网
>
> > 官网链接：https://material.io/
> >
> > 源码地址：https://github.com/material-components/material-components-android
> >
> > 示例下载：https://github.com/material-components/material-components-android/releases
> >
> > 这里面的都算官方控件，而且有很多，如果想实现某个功能时可以去看看是否已经有实现了的，他还专门写了一个实例 app，可以下下来看看，找到想要的再去看他的源码

## 二、动画