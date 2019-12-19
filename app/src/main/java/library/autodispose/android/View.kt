package library.autodispose.android

import android.view.View
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import library.autodispose.State
import library.autodispose.autoDispose
import library.autodispose.observable.ObservableProxy

private class Listener: View.OnAttachStateChangeListener {

    val subject by lazy {
        BehaviorSubject.create<State>()
    }

    override fun onViewDetachedFromWindow(v: View?) {
        subject.onNext(State.Destroyed)
        subject.onComplete()

        v?.removeOnAttachStateChangeListener(this)
    }

    override fun onViewAttachedToWindow(v: View?) {
        subject.onNext(State.Resumed)
    }
}

fun View.scope(): Observable<State> {
    val listener = Listener()
    addOnAttachStateChangeListener(listener)
    return listener.subject
}

fun <G> Observable<G>.autoDispose(view: View): ObservableProxy<G> {
    return autoDispose(view.scope())
}
