package library.autodispose.android

import io.reactivex.disposables.Disposable
import io.reactivex.subjects.PublishSubject
import org.junit.Test

import org.junit.Assert.*

class FragmentActivityTest {

    @Test
    fun testDoOnSubscribe() {
        val subject = PublishSubject.create<Unit>()

        var disposable2: Disposable? = null
        val disposable1 = subject
            .doOnSubscribe { disposable2 = it }
            .subscribe()

        disposable2?.dispose()

        println("[testDoOnSubscribe] disposable2, isDisposed = ${disposable2?.isDisposed}")
        println("[testDoOnSubscribe] disposable1, isDisposed = ${disposable1.isDisposed}")
        println("[testDoOnSubscribe] subject, hasComplete = ${subject.hasComplete()}")
    }

    @Test
    fun testPublish() {
        val subject = PublishSubject.create<Unit>()
        var value1: Unit? = null
        val disposable1 = subject.subscribe { value1 = it }

        val observable = subject.publish().refCount()
        var value2: Unit? = null
        val disposable2 = observable.subscribe { value2 = it }

        subject.onNext(Unit)
        println("[testPublish] value1 == value2? ${value1 == value2 && value1 != null}")

        disposable2.dispose()
        println("[testPublish] (1) disposable1, isDisposed = ${disposable1.isDisposed}")
        println("[testPublish] (1) disposable2, isDisposed = ${disposable2.isDisposed}")

        value2 = null
        subject.onNext(Unit)

        println("[testPublish] value1 == value2? ${value1 == value2 && value1 != null}")
    }

    @Test
    fun testPublish2() {
        val subject = PublishSubject.create<Unit>()
        val disposable1 = subject.subscribe()

        val observable = subject.publish().refCount()
        val disposable2 = observable.subscribe()

        disposable1.dispose()
        println("[testPublish] (2) disposable1, isDisposed = ${disposable1.isDisposed}")
        println("[testPublish] (2) disposable2, isDisposed = ${disposable2.isDisposed}")
    }

    @Test
    fun testPublish3() {
        val subject = PublishSubject.create<Unit>()
        val disposable1 = subject.subscribe()

        val observable = subject.publish().refCount()
        val disposable2 = observable.subscribe()

        subject.onComplete()
        println("[testPublish] (3) disposable1, isDisposed = ${disposable1.isDisposed}")
        println("[testPublish] (3) disposable2, isDisposed = ${disposable2.isDisposed}")
    }
}