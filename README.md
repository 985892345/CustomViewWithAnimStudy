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
> public class View {
> // 这个用于在代码中直接 new 一个 View，这个 context 一般都是 activity，代表上下文
> public View(Context context)
> 
> // 这个用于在 xml 中书写，系统自动将你写在 xml 中的属性装换为一个 AttributeSet 对象，然后调用这个函数
> public View(
> Context context, 
> @Nullable AttributeSet attrs
> )
> 
> // 这个一般用于设置默认属性，使用的情况较少
> // 系统不会主动调用，一般是开发者自己设置
> public View(
> Context context, 
> @Nullable AttributeSet attrs, 
> int defStyleAttr
> )
> 
> // 这个与上面的类似，也是设置默认属性，使用的情况也较少，且只支持 Android 5.0 以上
> // 系统不会主动调用
> public View(
> Context context, 
> @Nullable AttributeSet attrs, 
> int defStyleAttr, 
> int defStyleRes
> )
> }
> 
> ```
>
> 前面两个构造函数各位应该都能看懂，关键在于后面两个，他们与设置默认属性相关，在讲解前我们先得知道一个 View 可以通过哪些方式设置属性

#### 1、直接写在 XML 中

> ```xml
> <androidx.cardview.widget.CardView
> android:layout_width="200dp"
> android:layout_height="200dp"
> app:cardBackgroundColor="@android:color/darker_gray"
> app:cardCornerRadius="8dp"/>
> ```
>
> 这样就会给当前 View 设置属性
>
> 原理是系统自动把这些属性写入到 AttributeSet 类里面，然后调用
>
> ```kotlin
> public View(Context context, @Nullable AttributeSet attrs)
> ```
>
> 来生成 View 对象
>
> 

#### 2、使用 @style 设置属性

> 在 `style.xml` 中：
>
> ```xml
> <style name="myCardView_style">
>     <item name="cardBackgroundColor">@android:color/darker_gray</item>
>     <item name="cardCornerRadius">8dp</item>
> </style>
> ```
>
> 在 `layout.xml` 中：
>
> ```xml
> <androidx.cardview.widget.CardView
>  android:layout_width="200dp"
>  android:layout_height="200dp"
>  style="@style/myCardView_style"/>
> ```
>
> 这个一般用于多个相同控件复用属性的时候
>
> 原理与上一个是一样的，它会把 `style="@style/MyCardView"` 里写的属性一起写入 AttributeSet 类里面
>
> > 问题：如果 `style="@style/MyCardView"` 与 xml 中有相同属性它会怎么处理呢？
> >
> > 经过测试后，xml 中定义的属性会覆盖 `style="@style/MyCardView"` 中定义的属性
> >
> > 

#### 3、在 theme 中设置某种控件的默认属性

> 在 `theme.xml` 中：
>
> ```xml
> <style name="MyAppTheme" parent="Theme.MaterialComponents">
>     <!--下面这个 @style/MyCardView 就是前面写的 <style name="MyCardView">-->
>     <item name="cardViewStyle">@style/myCardView_style</item>
> </style>
> ```
>
> 这里 `name="cardViewStyle"` 意思是定义 CardView 的默认属性
> 这个默认属性是如何生效的呢？
>
> ```java
> // 原因在于 CardView 的构造函数
> public CardView(
>  @NonNull Context context, 
>  @Nullable AttributeSet attrs
> ) {
>  this(context, attrs, R.attr.cardViewStyle);
> }
> 
> public CardView(
>  @NonNull Context context, 
>  @Nullable AttributeSet attrs, 
>  int defStyleAttr
> ) {
>  super(context, attrs, defStyleAttr);
> }
> ```
>
> 这里我们可以发现，两个参数的构造函数使用了 `this(context, attrs, R.attr.cardViewStyle)` 调用了三个参数的构造函数，其中他给第三个参数传入了一个值 `R.attr.cardViewStyle`，我们去扒它源码，看下这是什么东西（直接点击导包中的 `import androidx.cardview.R` 就可以跳到源码中）
>
> ```xml
> <resources>
>  <attr format="reference" name="cardViewStyle"/>
> </resources>
> ```
>
> 这里可能你们会看不懂，我简单讲一下：这个 `format="reference"` 代表这个属性接受的类型，而 `reference` 表示接受的类型为一个引用值，比如：`@style/xxx`、`@color/xxx`、`@drawable/xxx` 就是引用值
>
> 然后 `name="cardViewStyle"` 代表这个属性的唯一名字
>
> 这个 attr 的引用就是 CardView 可以在 theme 中设置默认属性的原因，因为系统调用 CardView 两个参数的构造函数，然后它这个构造函数主动调用了三个参数的构造函数，并且传入了 `R.attr.cardViewStyle`，所以构造函数的第三个参数对应的是一个`style` 格式的引用，但需要你自己在 View 中指定名字
>
> 引用 `theme.xml` 有以下几种方式：
>
> - 全局引用，所有 View 都会使用该配置
>
>   ```xml
>   <application
>       android:theme="@style/MyAppTheme">
>   </application>
>   ```
>
> - 某个 Activity 引用，当前 ACtivity 内使用该配置
>
>   ```xml
>   <activity
>       android:name=".section3.test.TestActivity"
>       android:theme="@style/MyAppTheme"
>       android:exported="false" />
>   ```
>
> - 某个 Activity 使用代码引用
>
>   ```kotlin
>   override fun onCreate(savedInstanceState: Bundle?) {
>       super.onCreate(savedInstanceState)
>       setTheme(R.style.MyAppTheme)
>       setContentView(R.layout.layout_card0)
>   }
>   // 注意在 Activity 中使用必须在 setContentView 之前
>   ```
>
> - 单个 View 使用
>
>   ```xml
>   <androidx.cardview.widget.CardView
>       android:layout_width="200dp"
>       android:layout_height="200dp"
>       android:theme="@style/MyAppTheme" />
>   ```
>
> 看到这里你可能有点晕，我们简单梳理一下流程：
>
> ```flow
> read=>start: 系统读取你写的 xml
> new=>operation: 生成 AttributeSet，然后调用两个参数的构造函数
> first=>operation: 两个参数的调用三个参数的，并传入 R.attr.cardViewStyle
> second=>operation: View 开始从 theme 中读取属性
> third=>operation: View 在 theme 中发现 <item name="cardViewStyle">@style/myCardView_style</item> 的定义
> fourth=>end: 读取 @style/MyCardView 里面的属性
> read->new->first->second->third->fourth
> ```
>
> 

#### 4、调用构造函数的 defStyleRes

> 由于 CardView 没有重写第四个构造函数（可能是为了兼容 Android 5.0 以下？），所以这里我用 FrameLayout 来演示
>
> 先在 `style.xml` 中定义属性：
>
> ```xml
> <style name="myFrameLayout">
>  <item name="android:layout_width">match_parent</item>
>  <item name="android:layout_height">match_parent</item>
>  <item name="android:background">@android:color/black</item>
> </style>
> ```
>
> 然后在代码中这样使用：
>
> ```kotlin
> val frameLayout = FrameLayout(this, null, 0, R.style.myFrameLayout_style)
> ```
>
> 当你不能在 xml 中申明，只能在代码中动态生成时，就可以使用这种写法，有个优点就是使用 `style.xml` 可以进行属性的复用，但一般都用不到复用，这时候就可以使用 LayoutParams 和调用对应方法来代替：
>
> ```kotlin
> val frameLayout = FrameLayout(this)
> // Android 第一节课我们讲过，父布局可以给子布局设置属性
> // 且属性都以 layout_ 开头，所以 layout_width 和 layout_height 
> // 对应由 LayoutParams 来设置
> frameLayout.layoutParams = ViewGroup.LayoutParams(
>  ViewGroup.LayoutParams.MATCH_PARENT,
>  ViewGroup.LayoutParams.MATCH_PARENT
> )
> // background 属于 View 自身属性，所以对应 View 自身方法
> frameLayout.setBackgroundColor(Color.BLACK)
> 
> // 但这样写有一个缺点就是：LayoutParams 这东西只能设置少量的属性，
> // 而且 LayoutParams 有很多个，具体选择那个需要看你的 View 被添加到
> // 哪种父布局里，如果是 LinearLayout，就是 LinearLayout.LayoutParams(...)
> ```
>
> 可能有人不知道 LayoutParams 是什么东西，你暂时可以把它看成是用来保存 View 中有关**父布局属性**的一个数据类，供父布局使用，后面会再次讲解
>
> 除了上面这种动态生成时传入默认属性，你也可以在构造器中设置你的自定义 View 的默认属性：
>
> 一般是这样定义构造器的：
>
> ```kotlin
> // 由于 java 是基础，我先用 java 来定义构造器，后面再使用 kt 来优化写法
> public class Section3View extends View {
>  public Section3View(Context context) {
>      // 注意这里调用的是 this，而不是 super
>      this(context, null);
>  }
> 
>  public Section3View(
>      Context context, 
>      @Nullable AttributeSet attrs
>  ) {
>      // 注意这里调用的是 this，而不是 super
>      this(context, attrs, R.attr.mySection3View_attrs);
>  }
> 
>  public Section3View(
>      Context context, 
>      @Nullable AttributeSet attrs, 
>      int defStyleAttr
>  ) {
>      // 注意这里调用的是 this，而不是 super
>      // 这里就是设置默认属性
>      this(
>          context, 
>          attrs, 
>          defStyleAttr, 
>          R.style.mySection3View_defaultAttrs
>      );
>  }
> 
>  public Section3View(
>      Context context, 
>      @Nullable AttributeSet attrs, 
>      int defStyleAttr, 
>      int defStyleRes
>  ) {
>      // 直到这里才调用 super
>      super(context, attrs, defStyleAttr, defStyleRes);
>  }
> }
> ```
>
> 其中 `R.attr.mySection3View_attrs`：
>
> ```xml
> <!--value/attrs.xml-->
> <resources>
>  <attr name="mySection3View_attrs" format="reference"/>
> </resources>
> ```
>
> 其中 `R.style.mySection3View_defaultAttrs`：
>
> ```xml
> <!--value/style.xml-->
> <style name="mySection3View_defaultAttrs">
>  <item name="android:background">@android:color/black</item>
> </style>
> ```
>
> 如果你去对照官方控件的写法，你会发现他们的构造函数也是这样写的，前三个都是调用 `this()`，只有最后一个调用   `super()`，但他们一般只设置了 `R.attr.mySection3View_attrs`，用于开发者在 theme 中定义全局属性（[第三点](#3、在 theme 中设置某种控件的默认属性)），而 `R.style.mySection3View_defaultAttrs` 在官网控件中一般没设置，都是写个 0，代表没得默认属性
>
> > 为什么有两个默认属性？
> >
> > ```java
> > public View(
> > Context context, 
> > @Nullable AttributeSet attrs, 
> > int defStyleAttr, 
> > int defStyleRes
> > )
> > ```
> >
> > 你会发现第三个参数 `defStyleAttr` 和第四个 `defStyleRes` 都是设置默认参数，但为什么要同时存在两个呢？
> >
> > 原因：`defStyleAttr` 是从 theme 中读取默认值，但如果我们只能动态生成 View，又需要多次用到一些属性，在 Android 5.0 之前要么每次都手动调用方法来设置，要么写在 theme 中，但这样未免麻烦了些，在 Android 5.0 后增加了这个 `defStyleRes` 参数，就可以直接把属性写在 `style.xml` 中，方便复用
> >
> > **但这里有很重要的一点：**使用 `defStyleRes` 时不能设置 `defStyleAttr`，给 `defStyleAttr` 填入 0 或者 theme 中不设置对应的属性即可
>
> 回到这里，前面 java 的写法确实感觉有些臃肿了，我们来看看 kt 的写法：
>
> ```kotlin
> // 必须添加 @JvmOverloads，不然 java 层无法使用该构造器
> // 会导致 xml 中的 View 无法生成
> class Section3ViewKt @JvmOverloads constructor(
>     context: Context,
>     attrs: AttributeSet? = null,
>     defStyleAttr: Int = R.attr.mySection3View_attrs,
>     defStyleRes: Int = R.style.mySection3View_defaultAttrs
> ) : View(context, attrs, defStyleAttr, defStyleRes) {
> }
> // 如果你没得 R.attr.mySection3View 或 R.style.MySection3View_defaultAttrs
> // 可以直接赋值为 0，就是没得默认属性设置
> ```
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

#### 5、直接在 theme 中定义属性

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

> ### 作用
>
> 对 View 进行绘制
>
> ### 涉及内容
>
> - [Canvas 画布](#1、Canvas 画布)
> - [Paint 画笔](#2、Paint 画笔)
> - [Path 路径](#3、Path 路径)
>
> ```kotlin
> // 该方法是一个回调，为什么是回调，在之后的 invalidate() 会进行讲解
> override fun onDraw(canvas: Canvas?) {
> super.onDraw(canvas)
> }
> ```
>
> > 这里讲一个小技巧：可以把 `Canvas?` 中的 `?` 为去掉
> >
> > 因为在 kt 重写 `onDraw()` 方法时，源代码 java 层中并没有给参数 `canvas` 加上 `@NonNull`，导致 kt 不知道它会不会为空，但我们作为开发者肯定是知道它不会为空的，所以去掉 `?`，可以减少后续的判断，不然很可能写出这样的代码
> >
> > ```kotlin
> > override fun onDraw(canvas: Canvas?) {
> >     super.onDraw(canvas)
> >     canvas?.drawRect(0F, 0F, 100F, 200F, mBlackPaint)
> >     canvas?.drawRect(0F, 0F, 100F, 200F, mBlackPaint)
> >     canvas?.drawRect(0F, 0F, 100F, 200F, mBlackPaint)
> >     canvas?.drawRect(0F, 0F, 100F, 200F, mBlackPaint)
> >     canvas?.drawRect(0F, 0F, 100F, 200F, mBlackPaint)
> >     canvas?.drawRect(0F, 0F, 100F, 200F, mBlackPaint)
> > }
> > ```
> >
> > 看着好像没得什么问题，不就多打了一个 `?` 吗？
> >
> > 确实看着没有什么问题，但你把 kt 转成 java，你就会发现：
> >
> > ```java
> > protected void onDraw(@Nullable Canvas canvas) {
> >    super.onDraw(canvas);
> >    if (canvas != null) {
> >       canvas.drawRect(0.0F, 0.0F, 100.0F, 200.0F, this.mBlackPaint);
> >    }
> >    if (canvas != null) {
> >       canvas.drawRect(0.0F, 0.0F, 100.0F, 200.0F, this.mBlackPaint);
> >    }
> >    if (canvas != null) {
> >       canvas.drawRect(0.0F, 0.0F, 100.0F, 200.0F, this.mBlackPaint);
> >    }
> >    if (canvas != null) {
> >       canvas.drawRect(0.0F, 0.0F, 100.0F, 200.0F, this.mBlackPaint);
> >    }
> >    if (canvas != null) {
> >       canvas.drawRect(0.0F, 0.0F, 100.0F, 200.0F, this.mBlackPaint);
> >    }
> >    if (canvas != null) {
> >       canvas.drawRect(0.0F, 0.0F, 100.0F, 200.0F, this.mBlackPaint);
> >    }
> > }
> > ```
> >
> > 每个 canvas 都单独判断了一遍是否为空，不知道你们怎么想，但我的强迫症不允许出现这种情况，所以建议各位写自定义 View 时，把方法参数中可以去掉 `?` 的顺手给去掉
> >
> > 除了 `onDraw()` 以外，还有 `override fun onTouchEvent(event: MotionEvent?)` 的 `?` 也可以去掉
> >
> > > 怎么看 kt 转成的 java 代码？
> > >
> > >  <img src="https://gitee.com/guo985892345/typora/raw/master/img/image-20220320161207313.png" alt="image-20220320161207313" style="zoom:50%;" />
> > >
> > >  <img src="C:/Users/%E9%83%AD%E7%A5%A5%E7%91%9E/AppData/Roaming/Typora/typora-user-images/image-20220320144121673.png" alt="image-20220320144121673" style="zoom:50%;" />
> >
> > 接下来我们开始讲解 `onDraw()` 需要的其他知识
>

#### 1、Canvas 画布

> 由于时间原因，这里不会讲得很详细，你们可以在这些地方进行学习：
>
> - https://carsonho.blog.csdn.net/article/details/60598775
> - https://qijian.blog.csdn.net/article/details/50995268
>
>
> | 参数         | 类型   | 作用                                                         |
> | ------------ | ------ | ------------------------------------------------------------ |
> | ***canvas*** | Canvas | 字面翻译是“帆布”，你可以看成是系统底层给了你一张“画布”（canvas），然后你在这张画布上进行绘图 |
>
> 那怎么进行画图呢？
>
> ```kotlin
> // 这个 paint 你可以把它看成是“画笔”
> private val mBlackPaint = Paint().apply {
> color = Color.BLACK // 设置画笔颜色为黑色
> }
> override fun onDraw(canvas: Canvas) {
> super.onDraw(canvas)
> // 画一个 (0, 0) - (100, 200) 的黑色矩形
> canvas.drawRect(0F, 0F, 100F, 200F, mBlackPaint)
> }
> ```
>
> 如上面代码，调用 `canvas.drawRect()` 就可以画一个黑色矩形在 View 里面
>
> 接下来，我们看一下效果：
>
> <img src="https://gitee.com/guo985892345/typora/raw/master/img/image-20220320144833039.png" alt="image-20220320144833039" style="zoom:50%;float:left" />
>
> 如果你没有自定义 View 基础的话，可能会觉得有些奇怪
>
> 我们从小学开始学的坐标系第一象限不是向上为 Y 正半轴，向右为 X 正半轴，那么按照惯性思维，(0, 0) - (100, 200) 为什么不是下图这样的？
>
> <img src="https://gitee.com/guo985892345/typora/raw/master/img/image-20220320144833039.png" alt="image-20220320144833039" style="zoom:50%;transform:rotateX(180deg);float:left" />
>
> 其实原因在于 View 中坐标系的 y 轴是反过来的，我猜测是因为：为了设配手机的特点，比如我们看一些列表，都是手指往上滑动，查看下面的内容，所以为了好开发，View 中的坐标原点就设置在了左上角，向下为 Y 正半轴，向右为 X 正半轴
>
> 这个算是基础内容了，如果不知道的话，可以去看看这篇文章：https://blog.csdn.net/carson_ho/article/details/56009827
>
> Canvas 里面不止有绘制矩形的方法，还有绘制圆的方法，绘制文字的方法······，这里不会去讲解这些基础内容，可以去下面这些地方去查看：
>
> - https://developer.android.google.cn/reference/kotlin/androidx/compose/ui/graphics/Canvas?hl=en

#### 2、Paint 画笔

> 前面的代码演示中出现了 `Paint`，这个类主要是管画笔的，除了设置颜色以外，他还可以设置线的粗细等
>
> ```kotlin
> // 这是我的某个自定义 View 设置的 Paint
> private val mCircleBackgroundPaint by lazyUnlock {
>     Paint().apply {
>         color = mCircleBackground
>         style = Paint.Style.FILL
>         isAntiAlias = true
>     }
> }
> ```
>
> Paint 的方法有很多，这里给出文章，你们有时间可以去看看，那本自定义黑书对于 Paint 的内容有点少，建议去看书作者写的文章，里面有很多其他高级用法
>
> - https://qijian.blog.csdn.net/article/details/50995268

#### 3、Path 路径

> Path 表示路径，常用于绘制曲线，绘制不规则的图形等
>
> ```kotlin
> // 这是我的某个自定义 View 设置的 Path
> // 主要是写了根据变化的 offset 值来计算圆的半径，最后绘制一个圆
> private fun drawFirstCircle(path: Path, offset: Float, total: Float) {
>     val radio = abs(offset / total)
>     val startMove = 0.6F
>     val k = 1 / (1 - startMove)
>     val b = 1 - k
>     val y = offset / abs(offset) * max(0F, k * radio + b) * total
>     val r = getNewRadius(y)
>     val dx = (outerX - abs(y)) / (outerR + r) * r
>     val dy = outerY / (outerR + r) * r
>     firstPointX = if (offset > 0) dx + y else -dx + y
>     firstPointY = dy
>     path.addCircle(y, 0F, r, Path.Direction.CCW)
> }
> ```
>
> 方法有很多，这里不进行讲解，给出下面文章：
>
> - https://developer.android.google.cn/reference/kotlin/androidx/compose/ui/graphics/Path?hl=en
> - https://carsonho.blog.csdn.net/article/details/60597923
> - https://qijian.blog.csdn.net/article/details/50995268

#### 4、其他东西

> 上面并没有列完基础知识，因为基础知识实在是太多了，这里是写不下的，接下来我讲一些其他经验性的东西

##### 1、onDraw() 里面禁止 new 对象

> 很多初学者在刚开始写的时候很喜欢在 `onDraw()` 里面 new 对象，之前 `canvas.drawRect()` 不是要传入一个画笔吗？就会有人这样写
>
> ```kotlin
> override fun onDraw(canvas: Canvas) {
>  super.onDraw(canvas)
>  val blackPaint = Paint()
>  blackPaint.color = Color.BLACK
>  canvas.drawRect(0F, 0F, 100F, 200F, blackPaint)
> }
> ```
>
> 这样写时 AS 还会报一个黄，告诉你这样写是不对的，但你知道为什么不对吗？
>
> 原因：`onDraw()` 在一直刷新视图时，在 60 帧的手机上会每隔 16 毫秒回调一次，90 帧的手机上每隔 11 毫秒回调一次（1000 ÷ 90），所以在一秒钟，60 帧的手机会生成 60 个 Paint 对象，90 帧手机生成 90 个对象，这样疯狂 new 对象，会让手机出现卡顿，这跟上节课讲的 `onBindViewHolder()` 不要写点击监听一样，但这个会比它更严重
>
> 但 AS 的智能提示并不是能发现所有的问题，比如下面这样：
>
> ```kotlin
> override fun onDraw(canvas: Canvas) {
>  super.onDraw(canvas)
>  set { 
>      // ...
>  }
> }
> 
> private fun set(func: () -> Unit) {
>  func.invoke()
> }
> ```
>
> 这种情况较难发现，这样写每次都会生成一个匿名内部类，建议加上 `inline` 关键字
>
> **该禁令同样适用于 `onMeasure()`、`onLayout()`！**

##### 2、不要在 onDraw() 里面进行耗时操作

> 原因跟上面一样
>
> **该禁令同样适用于 `onMeasure()`、`onLayout()`！**

##### 3、不要持有 canvas 对象

> 每次回调的 canvas 对象并不是一定是同一个（大部分情况下是同一个），而且每次的回调只能表示当前帧的状态，比如我们来个违规操作：
>
> ```kotlin
> override fun onDraw(canvas: Canvas) {
>  super.onDraw(canvas)
>  // 开一个线程模拟持有 canvas 对象
>  thread {
>      // 延迟 20 毫秒才绘图
>      sleep(20)
>      canvas.drawRect(0F, 0F, 100F, 200F, mBlackPaint)
>  }
> }
> ```
>
> 你可能猜都猜不到它把图绘制到哪里去了
>
> 
>
> 
>
> 
>
> 
>
> 
>
> 
>
> 
>
> 
>
> 
>
> 
>
> <img src="https://gitee.com/guo985892345/typora/raw/master/img/image-20220320163052101.png" alt="image-20220320163052101" style="zoom:50%;float:left" />
>
> 好家伙，它竟然把图绘制到标题栏上了，这里猜测原因如下（没有去查看源码找真正原因）：
>
> - 整个应用共用了同一个 canvas 对象
> - 因开启线程后 sleep，所以 canvas 是整个应用绘制完成后再调用的 `canvas.drawRect()`
> - **标题栏是在最后进行绘制的**，此时的 canvas 的坐标系是以标题栏为准的
>
> 这怎么验证呢？
>
> > 你把开发者模式模式的 `显示布局边界` 给打开，你会看到下面这种图：
> >
> > <img src="https://gitee.com/guo985892345/typora/raw/master/img/image-20220320164459116.png" alt="image-20220320164459116" style="zoom: 67%;float:left" />
> >
> > 这里说明标题栏的文字是一个 View 来显示的
> >
> > 那我们试试把标题栏给去掉会发生什么？
> >
> > ```xml
> > <!--theme.xml-->
> > <style name="xxx" parent="Theme.MaterialComponents.DayNight.NoActionBar">
> > ```
> >
> > <img src="https://gitee.com/guo985892345/typora/raw/master/img/image-20220320164844556.png" alt="image-20220320164844556" style="zoom: 67%;float:left" />
> >
> > 嘿，去掉标题栏后绘制的位置对了，所以这里我提出猜测：标题栏是在最后进行绘制的
> >
> > 其实我们可以验证一下这个猜测，写一个在它后面绘制的 View 验证下：
> >
> > ```xml
> > <com.ndhzs.lib.section4.MySection4ViewCanvas
> >  android:layout_width="300dp"
> >  android:layout_height="300dp"
> >  app:layout_constraintBottom_toBottomOf="parent"
> >  app:layout_constraintLeft_toLeftOf="parent"
> >  app:layout_constraintRight_toRightOf="parent"
> >  app:layout_constraintTop_toTopOf="parent" />
> > 
> > <View
> >  android:layout_width="match_parent"
> >  android:layout_height="match_parent"/>
> > ```
> >
> > <img src="https://gitee.com/guo985892345/typora/raw/master/img/image-20220320165148269.png" alt="image-20220320165148269" style="zoom: 67%;float:left" />
> >
> > 果然，我的猜测应该是合理的

##### 4、自定义 View 和 ViewGroup 在绘制中的不同

> 在自定义 ViewGroup 中，一般不会重写 `onDraw()`，因为 ViewGroup 对 `onDraw()` 进行了处理，只有在**有背景图时**，才会调用 `onDraw()`，所以在自定义 ViewGroup 时想绘图一般是重写 `dispatchDraw(Canvas canvas)`，这个方法也可以决定是绘制在子 View 上层还是子 View 下层，具体的你们自己去实践一下吧，这里给出文章链接：https://www.jianshu.com/p/89efaf8bd3dd

#### 5、onDraw() 的绘制流程

> 这东西属于高阶内容，限于时间关系，这里我就不讲了，可以看看这些文章：
>
> - https://blog.csdn.net/carson_ho/article/details/56011153
> - https://github.com/Idtk/Blog/blob/master/Blog/9%E3%80%81Invalidate.md
>
> 

### 5、invalidate()

> 这东西你目前只需要记住以下几点就可以了：
>
> - 调用后会在**下一帧回调** `onDraw()` 进行刷新
> - 下一帧是指下一次屏幕刷新的时候
>
> 如果你想探究源码的话，我只能丢文章出来了：
>
> - https://juejin.cn/post/7017452765672636446
>
> OK，这个方法讲完了，因为往深了讲，会涉及到 Handler 机制、ViewRootImpl 等 Framwork 层源码
>
> 哦，想起了，还要一点，除了 `invalidate()` 外还有一个 `postInvalidate()` 用于在其他线程里刷新
>
> 但考虑到可能会有其他学长来听课，我还是画一个简单的流程图
>
> ```flow
> view=>start: View
> invalidate=>operation: 调用 invalidate() 刷新
> hasParent=>condition: parent != null
> viewGroup=>operation: 告诉父布局有子布局要重绘
> noParent=>operation: parent = null，则传递到了 ViewRootImpl (一个管理布局的类)
> viewRootImpl=>operation: ViewRootImpl 调用 Choreographer 发送一个 post (一个专门监听屏幕刷新的类)
> callback=>operation: 屏幕刷新了，回调 ViewRootImpl，开始重新走 View 测量和绘图流程
> foreach=>end: 从顶部布局最后回调到 View 的 onDraw()
> 
> view->invalidate->hasParent
> hasParent(yes)->viewGroup->hasParent
> hasParent(no)->noParent->viewRootImpl->callback->foreach
> ```
>
> 其实还有很多细节，这里我就不讲述了
>
> END

### 6、自定义属性 AttributeSet

> 前面我们提到系统会把你 xml 中写得属性装换为一个 AttributeSet 对象，那我们怎么自定义自己的属性呢？
>
> 首先，需要新建一个 `res/value/attrs.xml` 文件，比如我想要自定义一个圆半径的属性
>
> ```xml
> <resources>
>  <!--这个 name 是 View 的类名-->
>  <declare-styleable name="MySection6View">
>      <!--这个 name 是自定义属性的名称，formate 是接受的类型-->
>      <attr name="my6_circleRadius" format="dimension"/>
>  </declare-styleable>
> </resources>
> ```
>
> 如上述代码，这里有需要注意的一点：
>
> - 自定义属性的名称不能与官方的重复，而且建议加上前缀，与官方的进行区分，比如我就加上了 `my6_`
>
> `formate` 有下面几种类型：
>
> | 类型            | 说明     | 用法                          |                                        |
> | --------------- | -------- | ----------------------------- | -------------------------------------- |
> | ***dimension*** | 长度值   | "1dp"、"1px"、"@dimen/xxx"    | 这里无所谓 dp、px，系统会最终装换成 px |
> | ***reference*** | 引用值   | "@drawable/xxx"、"@style/xxx" |                                        |
> | ***color***     | 颜色值   | "#FFFFFFFF"、"@color/xxx"     |                                        |
> | ***float***     | 小数     | "1.0"                         |                                        |
> | ***boolean***   | 布尔类型 | "true"、"@bool/xxx"           |                                        |
> | ***interger***  | 整数     | "1"、"@interget/xxx"          |                                        |
> | ***string***    | 字符串   | "string"、"@string/xxx"       |                                        |
> | ***fraction***  | 百分数   | "50%"、"@fraction/xxx"        |                                        |
> | ***enum***      | 枚举     | [看下文](#enum)               |                                        |
> | ***flag***      | 位运算   | [看下文](#flag)               |                                        |
>
> ### enum
>
> > ```xml
> > <!--比如我设置圆的位置属性-->
> > <attr name="my6_circleGravity" format="enum">
> >     <enum name="left" value="0"/>
> >     <enum name="center" value="1"/>
> >     <enum name="right" value="2"/>
> > </attr>
> > ```
> >
> > <img src="https://gitee.com/guo985892345/typora/raw/master/img/image-20220320195658810.png" alt="image-20220320195658810" style="zoom:50%;float:left" />
> >
> > 设置后在使用时它的属性就只能选择你填写的那几个
>
> ### flag
>
> > 就是进行位运算，这里我们以 FrameLayout 的 `layout_gravity` 为例
> >
> > ```xml
> > <attr name="layout_gravity">
> >     <!--0011 0000-->
> >     <flag name="top" value="0x30" />
> >     <!--0101 0000-->
> >     <flag name="bottom" value="0x50" />
> >     <!--0000 0011-->
> >     <flag name="left" value="0x03" />
> >     <!--0000 0101-->
> >     <flag name="right" value="0x05" />
> >     <!--0001 0000-->
> >     <flag name="center_vertical" value="0x10" />
> >     <!--0000 0001-->
> >     <flag name="center_horizontal" value="0x01" />
> >     <!--0001 0001-->
> >     <flag name="center" value="0x11" />
> >     <flag name="start" value="0x00800003" />
> >     <flag name="end" value="0x00800005" />
> > </attr>
> > ```
> >
> > 你是否还记得你曾经这样使用过？
> >
> > ```xml
> > <FrameLayout
> >     android:layout_width="match_parent"
> >     android:layout_height="match_parent">
> > 
> >     <View
> >         android:layout_width="match_parent"
> >         android:layout_height="match_parent"
> >         android:layout_gravity="top|left"/>
> > 
> > </FrameLayout>
> > ```
> >
> > > 这里你会发现它把 `top|left` 报黄了，原因在于它推荐你使用 `start` 去代替 `left`，因为在部分国家是从右边往左边阅读文字的，所以，Android 为了国际化，就设计出了 `start` 代替 `left`，`end` 代替 `right`
>
> ### 类型的混用
>
> > 比如 `android:layout_width` 是这样定义的：
> >
> > ```xml
> > <attr name="layout_width" format="dimension">
> >     <enum name="fill_parent" value="-1" />
> >     <enum name="match_parent" value="-1" />
> >     <enum name="wrap_content" value="-2" />
> > </attr>
> > ```
> >
> > 
> >
> > 还有 `android:background` 是这样定义的：
> >
> > ```xml
> > <attr name="background" format="reference|color" />
> > ```
> >
> > 所以你传入 `@drawable/xxx` 和 `#FFFFFFFF`，它都可以获取，只是填入颜色值时使用 `TypedValue#getDrawable()` 得到是 ColorDrawable
> >
> > 
> >
> > 也支持这样混用：
> >
> > ```xml
> > <!--这是 View 的一个属性-->
> > <attr name="focusable" format="boolean|enum">
> >     <enum name="auto" value="0x00000010" />
> > </attr>
> > ```
> >
> > 但这样使用只能手动去判断：
> >
> > ```java
> > private int getFocusableAttribute(TypedArray attributes) {
> >     TypedValue val = new TypedValue();
> >     if (attributes.getValue(com.android.internal.R.styleable.View_focusable, val)) {
> >         if (val.type == TypedValue.TYPE_INT_BOOLEAN) {
> >             return (val.data == 0 ? NOT_FOCUSABLE : FOCUSABLE);
> >         } else {
> >             return val.data;
> >         }
> >     } else {
> >         return FOCUSABLE_AUTO;
> >     }
> > }
> > ```
>
> 前面讲了类型的定义，那我们怎么获取这些属性呢？
>
> ```kotlin
> private var mCircleRadius = 100
> // 这个 enum 属性更建议使用枚举来保存，官方通常使用的注解，但 kt 很怪，注解不起作用
> private var mCircleGravity = 0
> init {
>     if (attrs != null) {
>         // 主要就是调用下面这个方法去获取属性
>         val ty = context.obtainStyledAttributes(attrs, R.styleable.MySection6View)
>         mCircleRadius = ty.getDimensionPixelSize(
>             R.styleable.MySection6View_my6_circleRadius,
>             mCircleRadius
>         )
>         mCircleGravity = ty.getInt(
>             R.styleable.MySection6View_my6_circleGravity,
>             mCircleGravity
>         )
>         ty.recycle()
>     }
> }
> ```
>
> 这里还好只有两个属性，但如果有多达上百个属性这样写未免太占行数了，所以，秉持着少写代码的原则，我使用了 kt 扩展函数来优化（下面是我写的课表自定义 View）：
>
> ```kotlin
> // 先把属性的读取单独提取到一个类中
> open class NetLayout @JvmOverloads constructor(
>     context: Context,
>     attrs: AttributeSet? = null,
>     defStyleAttr: Int = R.attr.netLayoutStyle,
>     defStyleRes: Int = 0
> ) : ViewGroup(context, attrs, defStyleAttr, defStyleRes), INetLayout {
>     // 属性值
>     protected val mNetAttrs: NetLayoutAttrs = NetLayoutAttrs.newInstance(this, attrs)
> }
> ```
>
> ````kotlin
> // 我把属性单独写在了一个类里面
> open class NetLayoutAttrs(
>     rowCount: Int,
>     columnCount: Int
> ) : BaseViewAttrs {
> 
>     var rowCount = rowCount
>         internal set
>     var columnCount = columnCount
>         internal set
> 
>     companion object {
>         fun newInstance(
>             view: View,
>             attrs: AttributeSet?,
>             defStyleAttr: Int = 0,
>             defStyleRes: Int = 0,
>         ): NetLayoutAttrs {
>             return newAttrs(
>                 view,
>                 attrs,
>                 R.styleable.NetLayout,
>                 defStyleAttr,
>                 defStyleRes
>             ) {
>                 // 这里你就会看到直接使用 R.styleable.xxx.int() 来获取属性，这样写起来舒服多了
>                 NetLayoutAttrs(
>                     R.styleable.NetLayout_net_rowCount.int(ROW_COUNT),
>                     R.styleable.NetLayout_net_columnCount.int(COLUMN_COUNT)
>                 )
>             }
>         }
> 
>         const val ROW_COUNT = 4
>         const val COLUMN_COUNT = 4
>     }
> }
> ````
>
> ```kotlin
> // 这是定义扩展函数的接口
> internal interface BaseViewAttrs {
> 
>     fun <T> newAttrs(
>         view: View,
>         attrs: AttributeSet?,
>         @StyleableRes
>         styleableId: IntArray,
>         defStyleAttr: Int = 0,
>         defStyleRes: Int = 0,
>         func: Typedef.() -> T
>     ): T = BaseViewAttrs.newAttrs(view, attrs, styleableId, defStyleAttr, defStyleRes, func)
> 
>     companion object {
>         fun <T> newAttrs(
>             view: View,
>             attrs: AttributeSet?,
>             @StyleableRes
>             styleableId: IntArray,
>             defStyleAttr: Int = 0,
>             defStyleRes: Int = 0,
>             func: Typedef.() -> T
>         ): T {
>             val ty = view.context.obtainStyledAttributes(attrs, styleableId, defStyleAttr, defStyleRes)
>             if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
>                 // 这是保存在 Debug 模式中能看到的信息，具体怎么查看，你可以去看看这个方法的源码
>                 view.saveAttributeDataForStyleable(
>                     view.context, styleableId, attrs, ty, defStyleAttr, defStyleRes)
>             }
>             try {
>                 return Typedef(ty, view.context).func()
>             } finally {
>                 ty.recycle()
>             }
>         }
>     }
> 
>     class Typedef(val ty: TypedArray, private val context: Context) {
>         fun Int.int(defValue: Int): Int = this.int(ty, defValue)
>         fun Int.color(defValue: Int): Int = this.color(ty, defValue)
>         fun Int.colorById(@ColorRes defValueId: Int): Int = this.color(
>             ContextCompat.getColor(context, defValueId))
>         fun Int.dimens(defValue: Int): Int = this.dimens(ty, defValue)
>         fun Int.dimens(defValue: Float): Float = this.dimens(ty, defValue)
>         fun Int.layoutDimens(defValue: Int): Int = this.layoutDimens(ty, defValue)
>         fun Int.dimensById(@DimenRes defValueId: Int): Int = this
>         .dimens(context.resources.getDimensionPixelSize(defValueId))
>         fun Int.string(defValue: String? = null): String = this.string(ty, defValue)
>         fun Int.boolean(defValue: Boolean): Boolean = this.boolean(ty, defValue)
>         fun Int.float(defValue: Float): Float = this.float(ty, defValue)
>         internal inline fun <reified E: RuntimeException> Int.intOrThrow(
>             attrsName: String): Int = this.intOrThrow<E>(ty, attrsName)
>         internal inline fun <reified E: RuntimeException> Int.stringOrThrow(
>             attrsName: String): String = this.stringOrThrow<E>(ty, attrsName)
>     }
> }
> 
> internal fun Int.int(ty: TypedArray, defValue: Int): Int {
>     return ty.getInt(this, defValue)
> }
> 
> internal fun Int.color(ty: TypedArray, defValue: Int): Int {
>     return ty.getColor(this, defValue)
> }
> 
> internal fun Int.dimens(ty: TypedArray, defValue: Int): Int {
>     return ty.getDimensionPixelSize(this, defValue)
> }
> 
> internal fun Int.dimens(ty: TypedArray, defValue: Float): Float {
>     return ty.getDimension(this, defValue)
> }
> 
> internal fun Int.layoutDimens(ty: TypedArray, defValue: Int): Int {
>     return ty.getLayoutDimension(this, defValue)
> }
> 
> internal fun Int.string(ty: TypedArray, defValue: String? = null): String {
>     return ty.getString(this) ?: defValue ?: ""
> }
> 
> internal fun Int.boolean(ty: TypedArray, defValue: Boolean): Boolean {
>     return ty.getBoolean(this, defValue)
> }
> 
> internal fun Int.float(ty: TypedArray, defValue: Float): Float {
>     return ty.getFloat(this, defValue)
> }
> 
> internal inline fun <reified E: RuntimeException> Int.intOrThrow(
>     ty: TypedArray, attrsName: String
> ): Int {
>     if (!ty.hasValue(this)) {
>         throw E::class.java.getConstructor(String::class.java)
>             .newInstance("属性 $attrsName 没有被定义！")
>     }
>     return this.int(ty, 0)
> }
> 
> internal inline fun <reified E: java.lang.RuntimeException> Int.stringOrThrow(
>     ty: TypedArray, attrsName: String
> ): String {
>     if (!ty.hasValue(this)) {
>         throw E::class.java.getConstructor(String::class.java)
>             .newInstance("属性 $attrsName 没有被定义！")
>     }
>     return this.string(ty)
> }
> ```
>
> ### 自定义 layout_ 属性
>
> 之前在我给大家上的 Android 第一节课讲过，父 View 是可以给子 View 额外添加属性的，且属性名以 `layout_` 开头
>
> ```xml
> <!--比如说这个里面所有 layout_ 开头的都是父 View 给子 View 添加的额外属性-->
> <androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
>     android:layout_width="match_parent"
>     android:layout_height="match_parent"
>     xmlns:app="http://schemas.android.com/apk/res-auto">
> 
>     <FrameLayout
>         android:layout_width="match_parent"
>         android:layout_height="match_parent"
>         app:layout_constraintEnd_toEndOf="parent"
>         app:layout_constraintStart_toStartOf="parent"
>         app:layout_constraintTop_toTopOf="parent">
> 
>         <View
>             android:layout_width="match_parent"
>             android:layout_height="match_parent"
>             android:layout_gravity="top|left" />
> 
>     </FrameLayout>
> </androidx.constraintlayout.widget.ConstraintLayout>
> ```
>
> 那如何自定义 ViewGroup 如何设置自己想要的属性？
>
> 比如我给它添加一个表示子 View 位置的属性：
>
> ```xml
> <resources>
>     <!--注意：这种属性的设置必须以 _Layout 结尾，这是官方的规范！-->
>     <declare-styleable name="My6ViewGroup_Layout">
>         <!--注意：属性名建议带有 layout_ 开头的标识-->
>         <attr name="my6VG_layout_position" format="integer"/>
>     </declare-styleable>
> </resources>
> ```
>
> 然后在 xml 中写得时候，AS 会进行智能提示
>
> <img src="https://gitee.com/guo985892345/typora/raw/master/img/image-20220321194906364.png" alt="image-20220321194906364" style="zoom:67%;" />
>
> 那怎么获取这个属性呢？
>
> 这个我们留到 [LayoutParams](#1、LayoutParams) 再讲解

### 7、onMeasure()

> ### 作用
>
> 测量自身和子控件大小
>
> ### 涉及知识
>
> - MeasureSpecs
> - LayoutParams
>
> ```kotlin
> // 与上面讲的 onDraw() 一样，也是一个回调，该回调的作用是由 requestLayout() 触发
> override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
>     super.onMeasure(widthMeasureSpec, heightMeasureSpec)
> }
> ```

#### 1、LayoutParams

> 这就是一个很简单的数据类，用于在子 View 中保存父类需要的信息
>
> 我们直接来看一下 FrameLayout 的 LayoutParams
>
> ```java
> // 你会发现这 LayoutParams 很简单
> public static class LayoutParams extends MarginLayoutParams {
>     /**
>      * 对应 layout_gravity 属性没有被设置
>      */
>     public static final int UNSPECIFIED_GRAVITY = -1;
> 
>     /**
>      * 这就是 layout_gravity 属性的保存值
>      * @see android.view.Gravity
>      * @attr ref android.R.styleable#FrameLayout_Layout_layout_gravity
>      */
>     public int gravity = UNSPECIFIED_GRAVITY;
> 
>     public LayoutParams(@NonNull Context c, @Nullable AttributeSet attrs) {
>         super(c, attrs);
>         // 看到这里你应该就能回答之前留下的问题了
>         // 父 View 读取子 View 中的属性是在 LayoutParams 中获取的
>         // 可能你会奇怪于 View 是什么时候开始加载 LayoutParams
>         // 这个留到后面讲 setContentView 再讲解
>         final TypedArray a = c.obtainStyledAttributes(attrs, R.styleable.FrameLayout_Layout);
>         gravity = a.getInt(R.styleable.FrameLayout_Layout_layout_gravity, UNSPECIFIED_GRAVITY);
>         a.recycle();
>     }
> 
>     public LayoutParams(int width, int height) {
>         super(width, height);
>     }
> 
>     public LayoutParams(int width, int height, int gravity) {
>         super(width, height);
>         this.gravity = gravity;
>     }
> 
>     public LayoutParams(@NonNull ViewGroup.LayoutParams source) {
>         super(source);
>     }
> 
>     public LayoutParams(@NonNull ViewGroup.MarginLayoutParams source) {
>         super(source);
>     }
> 
>     public LayoutParams(@NonNull LayoutParams source) {
>         super(source);
>         this.gravity = source.gravity;
>     }
> }
> ```
>
> 可能你会觉得很奇怪，

#### 2、MeasureSpecs

> 推荐文章：https://www.jianshu.com/p/1260a98a09e9
>
> 它保存了测量模式和测量大小
>
> 比如上面代码中的：`widthMeasureSpec` 和 `heightMeasureSpec`
>
> 可能你会比较疑惑它是怎么用一个 int 来保存的，其实原理很简单，它使用高 2 位保存测量模式，后 30 为测量大小
>
> ```kotlin
> // 使用方式，原理就是位运算
> val wSize = MeasureSpec.getSize(widthMeasureSpec)
> val wMode = MeasureSpec.getMode(widthMeasureSpec)
> ```
>
> 测量模式有以下三种：
>
> | 模式              | 说明       | 应用场景                                     |
> | ----------------- | ---------- | -------------------------------------------- |
> | ***EXACTLY***     | 有具体值时 | 一般对应 `match_parent` 和具体的值           |
> | ***AT_MOST***     | 自适应大小 | 一般对应 `wrap_content`                      |
> | ***UNSPECIFIED*** | 可任意取值 | 只出现于 `ScrollView`、`ListView` 这类控件中 |
>
> 重写 ViewGroup 时就是通过这些来判断子 View 应该取得的高度的
>
> 关于怎么取，可以看这个在 ViewGroup 中的方法，里面是常见情况时的取法，该方法在很多官方控件中被使用
>
> ```java
> /**
>  * @param spec 子 View 能得到的 MeasureSpecs，一般是父 View 的测量模式 + 想给出的大小
>  * @param padding 间距值，一般这样填入：layoutParams.leftMargin + layoutParams.rightMargin
>  * @param childDimension 子 View 的宽或者高，固定填入：child.width 或者 child.height
>  */
> public static int getChildMeasureSpec(int spec, int padding, int childDimension) {
>     int specMode = MeasureSpec.getMode(spec); // 测量模式
>     int specSize = MeasureSpec.getSize(spec); // 测量长度
> 
>     int size = Math.max(0, specSize - padding); // 先减去间距值得到的最大长度
> 
>     int resultSize = 0; // 保存最后的长度值
>     int resultMode = 0; // 保存最后的测量，模式
> 
>     switch (specMode) {
>     // 如果测量模式是具体值，即一般对应父 View 为 match_parent 或者 确定值
>     case MeasureSpec.EXACTLY:
>         if (childDimension >= 0) {
>             // 假设子 View 固定为 100dp
>             resultSize = childDimension;
>             resultMode = MeasureSpec.EXACTLY;
>         } else if (childDimension == LayoutParams.MATCH_PARENT) {
>             // 子 View 是 match_parent
>             resultSize = size; // 那结果值就是最大值
>             resultMode = MeasureSpec.EXACTLY; // 测量模式保持一样，为具体值
>         } else if (childDimension == LayoutParams.WRAP_CONTENT) {
>             // 子 View 是 wrap_content
>             resultSize = size;
>             resultMode = MeasureSpec.AT_MOST; // 改变测量模式为自适应大小
>         }
>         break;
> 
>     // 如果测量模式是自适应大小，即一般对应父 View 为 wrap_content
>     case MeasureSpec.AT_MOST:
>         if (childDimension >= 0) {
>             // 假设子 View 固定为 100dp
>             resultSize = childDimension;
>             resultMode = MeasureSpec.EXACTLY;
>         } else if (childDimension == LayoutParams.MATCH_PARENT) {
>             // 子 View 是 match_parent
>             // 这里比较重要
>             // 这里对应父 View 为 wrap_content，但子 View 却为 match_parent
>             resultSize = size; // 这里的意思是给出子 View 能得到的最大值
>             resultMode = MeasureSpec.AT_MOST;
>         } else if (childDimension == LayoutParams.WRAP_CONTENT) {
>             // 子 View 是 wrap_content
>             resultSize = size;
>             resultMode = MeasureSpec.AT_MOST;
>         }
>         break;
> 
>     // 如果测量模式是可任意取值，即一般对应父 View 为 ScrollView
>     case MeasureSpec.UNSPECIFIED:
>         if (childDimension >= 0) {
>             // 假设子 View 固定为 100dp
>             resultSize = childDimension;
>             resultMode = MeasureSpec.EXACTLY;
>         } else if (childDimension == LayoutParams.MATCH_PARENT) {
>             // 子 View 是 match_parent
>             // 这个 sUseZeroUnspecifiedMeasureSpec 变量用于兼容 Android 旧版本
>             resultSize = View.sUseZeroUnspecifiedMeasureSpec ? 0 : size;
>             resultMode = MeasureSpec.UNSPECIFIED;
>         } else if (childDimension == LayoutParams.WRAP_CONTENT) {
>             // 子 View 是 wrap_content
>             resultSize = View.sUseZeroUnspecifiedMeasureSpec ? 0 : size;
>             resultMode = MeasureSpec.UNSPECIFIED;
>         }
>         break;
>     }
>     return MeasureSpec.makeMeasureSpec(resultSize, resultMode);
> }
> ```

##### 1、FrameLayout 的 `onMeasure() 源码分析`

> ```java
> // 接下来是起飞环节
> @Override
> protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
>     int count = getChildCount();
> 
>     // 判断自身是否有一边不是具体值，作用如下：
>     // 比如 FrameLayout 的宽是 wrap_content，但其中一个子 View 宽为 match_parent
>     // 那么这个时候 FrameLayout 是不知道该给这个子 View 宽度多少的
>     // 但我们可以通过其他 View 来判断 FrameLayout 能得到的宽度
>     // 这个时候就知道 FraneLayout 宽度值能取多少了
>     final boolean measureMatchParentChildren =
>             MeasureSpec.getMode(widthMeasureSpec) != MeasureSpec.EXACTLY ||
>             MeasureSpec.getMode(heightMeasureSpec) != MeasureSpec.EXACTLY;
>     // 这个是保存上述所说的那种子 View，用于二次测量
>     mMatchParentChildren.clear(); 
> 
>     int maxHeight = 0;
>     int maxWidth = 0;
>     // 状态值，用来记录子 View 是否得到了想要的大小，用的很少，忽略即可
>     int childState = 0; 
> 
>     // 遍历所有子 View
>     for (int i = 0; i < count; i++) {
>         final View child = getChildAt(i);
>         if (mMeasureAllChildren || child.getVisibility() != GONE) {
>             // measureChildWithMargins() 里面调用了测量子 View 的方法
>             // 之后会提到该方法，你只需要知道调用后可以得到子 View 测量的宽和高
>             // 但这个宽和高不是 width 和 height，而是 measuredWidth 和 measuredHeight
>             measureChildWithMargins(child, widthMeasureSpec, 0, heightMeasureSpec, 0);
>             // 得到 LayoutParams，主要是从里面取得定义的 Margin 值
>             final LayoutParams lp = (LayoutParams) child.getLayoutParams();
>             // 保存子 View 中最大的宽度
>             maxWidth = Math.max(maxWidth,
>                     child.getMeasuredWidth() + lp.leftMargin + lp.rightMargin);
>             // 保存子 View 中最大的高度
>             maxHeight = Math.max(maxHeight,
>                     child.getMeasuredHeight() + lp.topMargin + lp.bottomMargin);
>             // 状态值，忽略即可，在 ViewRootImpl 中有使用，一般情况下自定义 View 不用
>             childState = combineMeasuredStates(childState, child.getMeasuredState());
>             // 最开始的那个 boolean 值，用来保存是 match_parent 的 View
>             if (measureMatchParentChildren) {
>                 if (lp.width == LayoutParams.MATCH_PARENT ||
>                         lp.height == LayoutParams.MATCH_PARENT) {
>                     mMatchParentChildren.add(child);
>                 }
>             }
>         }
>     }
> 
>     // 最大值加上 padding 值
>     // getPadding*WithForeground() 是内部方法，我们自己用时是使用 getPadding*() 代替
>     maxWidth += getPaddingLeftWithForeground() + getPaddingRightWithForeground();
>     maxHeight += getPaddingTopWithForeground() + getPaddingBottomWithForeground();
> 
>     // 检查最小值的设置
>     // View 都自带了一个 android:minHeight 属性，自己自定义 View 时建议把这个适配一下
>     maxHeight = Math.max(maxHeight, getSuggestedMinimumHeight());
>     maxWidth = Math.max(maxWidth, getSuggestedMinimumWidth());
> 
>     // 得到前台的背景图，再与最大值比较
>     final Drawable drawable = getForeground();
>     if (drawable != null) {
>         maxHeight = Math.max(maxHeight, drawable.getMinimumHeight());
>         maxWidth = Math.max(maxWidth, drawable.getMinimumWidth());
>     }
> 
>     // 关键方法，调用 setMeasuredDimension() 设置自身宽和高
>     setMeasuredDimension(resolveSizeAndState(maxWidth, widthMeasureSpec, childState),
>             resolveSizeAndState(maxHeight, heightMeasureSpec,
>                     childState << MEASURED_HEIGHT_STATE_SHIFT));
> 
>     // 在上面那个调用过后，就能得到自身的宽和高了
>     // 然后这里再重写测量为 match_parent 的 View
>     count = mMatchParentChildren.size();
>     // 这个 count 判断我个人感觉有点问题
>     // count 是指为 match_parent View 的数量，
>     // 而如果我只有一个 View 是 match_parent，那它不是就不会重新测量了吗？
>     // 真奇怪，我感觉该用 childCount
>     if (count > 1) {
>         for (int i = 0; i < count; i++) {
>             final View child = mMatchParentChildren.get(i);
>             final MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
> 
>             final int childWidthMeasureSpec;
>             if (lp.width == LayoutParams.MATCH_PARENT) {
>                 final int width = Math.max(0, getMeasuredWidth()
>                         - getPaddingLeftWithForeground() - getPaddingRightWithForeground()
>                         - lp.leftMargin - lp.rightMargin);
>                 childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(
>                         width, MeasureSpec.EXACTLY);
>             } else {
>                 childWidthMeasureSpec = getChildMeasureSpec(widthMeasureSpec,
>                         getPaddingLeftWithForeground() + getPaddingRightWithForeground() +
>                         lp.leftMargin + lp.rightMargin,
>                         lp.width);
>             }
> 
>             final int childHeightMeasureSpec;
>             if (lp.height == LayoutParams.MATCH_PARENT) {
>                 final int height = Math.max(0, getMeasuredHeight()
>                         - getPaddingTopWithForeground() - getPaddingBottomWithForeground()
>                         - lp.topMargin - lp.bottomMargin);
>                 childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(
>                         height, MeasureSpec.EXACTLY);
>             } else {
>                 childHeightMeasureSpec = getChildMeasureSpec(heightMeasureSpec,
>                         getPaddingTopWithForeground() + getPaddingBottomWithForeground() +
>                         lp.topMargin + lp.bottomMargin,
>                         lp.height);
>             }
> 
>             child.measure(childWidthMeasureSpec, childHeightMeasureSpec);
>         }
>     }
> }
> ```
>
> 

##### 2、NestedScroll 嵌套 Rv 复用被取消

> 接下来开始做火箭
>
> 我们来分析一下我寒假时多次提到 NestedScrollView 嵌套 Rv，导致 Rv 复用失效的问题，网上我还没有看过很详细的从源码角度来分析的文章
>
> 首先我们要想想问什么会失效？
>
> 失效说明 Rv 把全部子 View 都测量完了，那说明肯定是 `onMeasure()` 的问题，ok，那我们来 debug 一下 `onMeasure()` 的整个流程
>
> ##### 首先从 NestedScrollView 的 onMeasure() 开始 debug
>
> 。。。。。。

### 

### 8、onLayout()

### 9、requestLayout()

> 这东西跟前面的 `invalidate()` 一样，你们只需要记住以下几点：
>
> - 调用后会在下一帧回调 `onMeasure()` 和 `onMeasure()` 进行重新测量和布局
> - 如果你的 View 大小发生改变，它还会调用 `onDraw()` 进行刷新
>
> 文章的话，与 `invalidate()` 的一样：https://juejin.cn/post/7017452765672636446

### 10、setContentView

#### 1、LayoutInflater

### 11、发布开源库

### 12、分享一些东西

> 谁想写自定义 View 啊，你想写吗？正经人写什么自定义 View。
>
> 自定义 View 确实很难，东西又多又杂，在考虑写自定义 View 之前，请先搜索一下是否有别人已经造好的轮子，有轮子直接用，他不香吗？[狗头]
>
> 这里分享一些轮子：
>

#### 1、Material Design 官网

> 官网链接：https://material.io/
>
> 源码地址：https://github.com/material-components/material-components-android
>
> 示例下载：https://github.com/material-components/material-components-android/releases
>
> 这里面的都算官方控件，而且有很多，如果想实现某个功能时可以去看看是否已经有实现了的，他还专门写了一个实例 app，可以下下来看看，找到想要的再去看他的源码

#### 2、View#post()、posyDelay()

#### 3、View 的生命周期

> 生命周期
>
> 如何监听 View 被 remove

#### 4、自定义 View 的一些规范

## 二、动画