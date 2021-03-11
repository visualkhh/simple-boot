import {Sim} from '@src/com/simple/boot/decorators/SimDecorator'
import {Module} from '@src/com/simple/boot/module/Module'
import {AjaxService} from '@src/com/simple/boot/service/AjaxService'
import html from './hello-world.html'
import {fromEvent} from 'rxjs'
import {I18nService} from '@src/app/features/service/I18nService'

@Sim()
export class HelloWord extends Module {
    template = html

    public numbers = [1, 2, 3, 4, 5, 6, 7]
    private admins = new class extends Module {
        selector = '#admins'
        public datas: any[] = []
        template = `
            <ul>
                {{#each datas as |data i|}}
                    <li>{{data.seq}}, {{data.name}}</li>
                {{/each}}
            </ul>
        `
    }()

    constructor(public i18nService: I18nService, public ajaxService: AjaxService) {
        super()
    }

    onInit() {
        this.loadData()
    }

    loadData() {
        this.ajaxService.getJSON('/admins').subscribe(it => {
            this.admins.datas = it as any[]
        })
    }

    onChangeRendered() {
        this.i18nService.renderSubscribe(it => {});
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
    }
}
