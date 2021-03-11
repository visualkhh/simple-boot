import 'reflect-metadata'
import {ConstructorType} from '@src/com/simple/boot/types/Types'
import {NoSuchSim} from '@src/com/simple/boot/throwable/NoSuchSim'
import {SimProxyMethodHandler} from '@src/com/simple/boot/proxy/SimProxyMethodHandler'
import {Module} from '@src/com/simple/boot/module/Module'

export const SimstanceManager = new class {
    private _storege = new Map<ConstructorType<any>, any>()

    get storege(): Map<ConstructorType<any>, any> {
        return this._storege
    }

    getSim<T>(k: ConstructorType<T>): T {
        let newVar = this.storege.get(k)
        if (!newVar) {
            newVar = this.resolve(k)
        }
        return newVar
    }

    register(target: ConstructorType<any>): void {
        const registed = this._storege.get(target)
        if (!registed) {
            this._storege.set(target, undefined)
        }
    }

    resolve<T>(target: ConstructorType<any>): T {
        const registed = this._storege.get(target)
        if (registed) {
            return registed as T
        }

        if (this._storege.has(target) && undefined === registed) {
            const paramTypes = Reflect.getMetadata('design:paramtypes', target) || []
            const injections = paramTypes.map((token: ConstructorType<any>) => {
                return SimstanceManager.resolve<any>(token)
            })
            let r = new target(...injections)
            for (const key in r) {
                if (r[key] instanceof Module) {
                    r[key] = new Proxy(r[key], new SimProxyMethodHandler())
                }
            }

            if (r instanceof Module) {
                r = new Proxy(r, new SimProxyMethodHandler())
            }
            this._storege.set(target, r)
            return r
        }
        throw new NoSuchSim('no simple instance')
    }
}()
