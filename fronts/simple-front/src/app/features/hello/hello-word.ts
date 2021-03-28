import {Sim} from '@src/com/simple/boot/decorators/SimDecorator'
import {Module} from '@src/com/simple/boot/module/Module'
import {AjaxService} from '@src/com/simple/boot/service/AjaxService'
import html from './hello-world.html'
import {fromEvent} from 'rxjs'
import {I18nService} from '@src/app/features/service/I18nService'
import {LoopModule} from '@src/app/shareds/LoopModule'
import {SimstanceManager} from '@src/com/simple/boot/simstance/SimstanceManager'
import css from './hello-word.css'
@Sim()
export class HelloWord extends Module {
    template = html
    styleImports = [css]
    public numbers = [1, 2, 3, 4, 5, 6, 7]
    private admins = new class extends Module {
        public datas: any[] = []

        constructor() {
            super('admins', `
            <ul>
                {{#each datas as |data i|}}
                    <li>{{data.seq}}, {{data.name}}</li>
                {{/each}}
            </ul>`)
        }
    }()

    public amodule = new LoopModule('amodule')
    public bmodule = new LoopModule('bmodule')

    constructor(public i18nService: I18nService, public ajaxService: AjaxService, public simstance: SimstanceManager) {
        super()
        console.log('--->', simstance);
    }

    onInit() {
        this.bmodule = new LoopModule('bmodule')
        this.loadData()
    }

    loadData() {
        this.ajaxService.getJSON('/admins').subscribe(it => {
            this.admins.datas = it as any[]
        })
    }

    onChangedRendered() {
        this.i18nService.renderSubscribe(it => {
        })
        fromEvent(document.querySelector('#save')!, 'click').subscribe(it => {
            const nameText = document.querySelector('#name') as HTMLInputElement
            this.ajaxService.postJson('/admin', {name: nameText.value}).subscribe(it => {
                this.loadData()
            })
        })
        fromEvent(document.querySelector('#language-save')!, 'click').subscribe(it => {
            const languageText = document.querySelector('#language') as HTMLInputElement
            this.i18nService.reloadAndRender(languageText.value)
        })

        fromEvent(document.querySelector('#module-data-change')!, 'click').subscribe(it => {
            this.amodule.datas = [5, 6, 7, 8, 9]
            this.bmodule.datas = [1, 4, 6, 87, 5, 6, 7, 8, 9]
        })
    }
}
