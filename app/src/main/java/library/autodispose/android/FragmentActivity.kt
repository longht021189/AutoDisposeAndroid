package library.autodispose.android

import androidx.lifecycle.*
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import library.autodispose.State
import library.autodispose.autoDispose
import library.autodispose.observable.ObservableProxy

private val factory by lazy {
    ViewModelProvider.NewInstanceFactory()
}

private class AutodisposeVM : ViewModel(), LifecycleObserver {

    val subject by lazy {
        BehaviorSubject.create<State>()
    }

    private var isCreated = false
    private var isCleared = false
    private var lifecycle: Lifecycle? = null

    @Synchronized
    @OnLifecycleEvent(value = Lifecycle.Event.ON_CREATE)
    fun onCreate() {
        if (isCleared) return

        if (!isCreated) {
            isCreated = true
            subject.onNext(State.Created)
        }
    }

    @Synchronized
    @OnLifecycleEvent(value = Lifecycle.Event.ON_RESUME)
    fun onResume() {
        if (isCleared) return

        subject.onNext(State.Resumed)
    }

    @Synchronized
    @OnLifecycleEvent(value = Lifecycle.Event.ON_PAUSE)
    fun onPause() {
        if (isCleared) return

        subject.onNext(State.Paused)
    }

    @Synchronized
    @OnLifecycleEvent(value = Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        if (isCleared) return

        subject.onNext(State.Paused)
    }

    @Synchronized
    override fun onCleared() {
        isCleared = true

        lifecycle?.removeObserver(this)
        lifecycle = null

        subject.onNext(State.Destroyed)
        subject.onComplete()
    }

    @Synchronized
    fun setupLifecycle(lifecycle: Lifecycle) {
        if (isCleared) return
        if (lifecycle !== this.lifecycle) {
            this.lifecycle = lifecycle
            lifecycle.addObserver(this)
        }
    }
}

fun <T> T.scope(): Observable<State> where T: ViewModelStoreOwner, T: LifecycleOwner {
    val owner = this
    val vmProvider = ViewModelProvider(owner, factory)
    val viewModel = vmProvider.get(AutodisposeVM::class.java)

    viewModel.setupLifecycle(lifecycle)

    return viewModel.subject
}

fun <G, T> Observable<G>.autoDispose(owner: T): ObservableProxy<G> where T: ViewModelStoreOwner, T: LifecycleOwner {
    return autoDispose(owner.scope())
}
