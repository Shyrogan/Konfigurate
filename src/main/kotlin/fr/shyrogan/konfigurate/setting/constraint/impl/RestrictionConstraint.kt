package fr.shyrogan.konfigurate.setting.constraint.impl

import fr.shyrogan.konfigurate.setting.SettingBuilder
import fr.shyrogan.konfigurate.setting.constraint.Constraint

/**
 * A [Constraint] used to restrict the values to certain [choices].
 */
class RestrictionConstraint<T: Any>(val choices: Array<out T>): Constraint<T> {

    override fun invoke(current: T, future: T): T {
        if(!choices.contains(future))
            return current

        return future
    }

    fun find(current: T, offset: Int): T {
        // Is there any better way to do it?
        val lastIndice = (choices.size - offset)
        var futureIndex = choices.indexOf(current) % lastIndice
        while (futureIndex < 0)
            futureIndex += lastIndice

        return choices[futureIndex]
    }

}

/**
 * Creates and adds a new [RestrictionConstraint]
 */
inline fun <reified T: Any> SettingBuilder<T>.restrict(vararg choices: T) {
    constraints += if(!choices.contains(defaultValue)) {
        RestrictionConstraint(arrayOf(defaultValue, *choices))
    } else {
        RestrictionConstraint(choices)
    }
}