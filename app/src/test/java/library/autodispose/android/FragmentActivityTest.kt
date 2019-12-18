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
}