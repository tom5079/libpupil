import org.mozilla.javascript.Context
import org.mozilla.javascript.Scriptable

internal fun <R> enterContext(block: (cx: Context) -> R): R? {
    val result: R?
    val cx = Context.enter().apply {
        optimizationLevel = -1
    }

    try {
        result = block.invoke(cx)
    } finally {
        Context.exit()
    }

    return result
}

internal fun <R> enterScope(block: (cx: Context, scope: Scriptable) -> R): R? =
    enterContext { cx ->
        val scope = cx.newObject(sharedScope).apply {
            prototype = sharedScope
            parentScope = null
        }

        block.invoke(cx, scope)
    }

internal val sharedScope = enterContext { cx ->
    cx.initSafeStandardObjects()
} ?: error("Failed to initialize JS Scope")