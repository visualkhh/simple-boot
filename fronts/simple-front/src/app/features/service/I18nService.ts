import {Sim} from '@src/com/simple/boot/decorators/SimDecorator'
import {AjaxService} from '@src/com/simple/boot/service/AjaxService'
import {BehaviorSubject, Observable} from 'rxjs'
import {Module} from '@src/com/simple/boot/module/Module'
import Handlebars from 'handlebars'
import {map} from 'rxjs/operators'

export class I18nModule extends Module {
    wrapElement = 'span';
    template = '{{data}}'
    constructor(public selector: string, public data: string) {
        super();
    }
}

export interface I18nModuleProperty {
    [name: string]: I18nModule
}

@Sim()
export class I18nService {
    public i18n$ = new BehaviorSubject(undefined as unknown as I18nModuleProperty)

    constructor(public ajaxService: AjaxService) {
        this.reload();
        Handlebars.registerHelper('i18n', (aString) => {
            const i18nModule = new I18nModule(this.makeKey(aString), aString)
            return i18nModule.renderWrapString();
        });
    }

    public reloadAndRender(lang = 'ko'): void {
        this.requestObservable(lang).pipe(
            map(it => this.transI18nModule(it)),
            map(it => {
                for (const key in it) {
                    it[key].render()
                }
                return it
            })
        ).subscribe(it => {
            this.publishI18nModule(it);
        })
    }

    public reload(lang = 'ko') {
        this.requestObservable(lang).pipe(
            map(it => this.transI18nModule(it))
        ).subscribe(it => {
            this.publishI18nModule(it);
        })
    }

    public requestObservable(lang = 'ko'): Observable<{ [key: string]: string }> {
        return this.ajaxService.getJSON<{ [key: string]: string }>(`/i18n?lang=${lang}`)
    }

    public transI18nModule(i18n: { [key: string]: string }): I18nModuleProperty {
        const rtn = {} as I18nModuleProperty
        for (const key in i18n) {
            const selector = this.makeKey(key)
            rtn[key] = new I18nModule(selector, i18n[key]);
        }
        return rtn;
    }

    public publishI18nModule(i18n: I18nModuleProperty) {
        this.i18n$.next(i18n);
    }

    public makeKey(key: string): string {
        return '__I18n__' + key;
    }

    subscribe(next?: (value: I18nModuleProperty) => void, error?: (error: any) => void, complete?: () => void) {
        return this.i18n$.subscribe(next, error, complete)
    }

    renderSubscribe(next?: (value: I18nModuleProperty) => void, error?: (error: any) => void, complete?: () => void) {
        return this.i18n$.pipe(
            map(it => {
                for (const key in it) {
                    it[key].render()
                }
                return it;
            })
        ).subscribe(next, error, complete)
    }
}
