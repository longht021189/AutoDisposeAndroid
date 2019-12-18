package library.autodispose.android

import io.reactivex.disposables.Disposable
import io.reactivex.functions.Action
import io.reactivex.functions.Consumer
import library.autodispose.observable.ObservableProxy

class EmptyObservableProxy<T>: ObservableProxy<T> {
    override fun doOnSubscribe(onSubscribe: Consumer<Disposable>): ObservableProxy<T> {
        return this
    }

    override fun subscribe(onNext: Consumer<T>?, onError: Consumer<Throwable>?, onComplete: Action?, onSubscribe: Consumer<Disposable>?): Disposable {
        return object : Disposable {
            override fun isDisposed(): Boolean {
                return true
            }

            override fun dispose() {}
        }
    }
}