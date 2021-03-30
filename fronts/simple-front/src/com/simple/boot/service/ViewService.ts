import {Sim} from '../../../../com/simple/boot/decorators/SimDecorator'
import {fromEvent, Observable} from 'rxjs'

export class View<T extends Element> {
    constructor(public e: T) {
    }

    event<T>(eventName: string): Observable<T> {
        return fromEvent<T>(this.e, eventName)
    }

    click<E>(e: T = this.e): Observable<E> {
        return fromEvent<E>(e, 'click');
    }

    get value() {
        return (this.e as any).value
    }
}

@Sim()
export class ViewService {
    constructor() {
    }

    e<T extends Element>(selector: string): View<T> | undefined {
        try {
            const querySelector = document.querySelector<T>(selector)
            if (querySelector) {
                return new View<T>(querySelector)
            } else {
                return undefined
            }
        } catch (e) {
            return undefined
        }
    }

    eI<T extends Element = Element>(selector: string): View<T> | undefined {
        return this.e(`#${selector}`) ?? undefined
    }

    eC<T extends Element = Element>(selector: string): View<T> | undefined {
        return this.e(`.${selector}`) ?? undefined
    }
}
