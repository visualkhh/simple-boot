import {Sim} from '@src/com/simple/boot/decorators/SimDecorator'
import {Module} from '@src/com/simple/boot/module/Module'
import {AjaxService} from '@src/com/simple/boot/service/AjaxService'
import html from './hello-world.html'
import {Renderer} from '@src/com/simple/boot/render/Renderer'
import {fromEvent} from 'rxjs'
@Sim()
export class HelloWord extends Module {
    public numbers = [1, 2, 3, 4, 5, 6, 7];
    private admins: any[] = [];
    constructor(public ajaxService: AjaxService) {
        super();
    }

    onInit() {
        this.loadData();
    }

    loadData() {
        this.ajaxService.getJSON('/admins').subscribe(it => {
            this.admins = it as any[]
            console.log('ajaxService -admins->', this.admins);
            Renderer.render(this);
        })
    }

    onChangeRendered() {
        const saveBtn = document.querySelector('#save');
        const nameText = document.querySelector('#name') as HTMLInputElement;
        console.log('saveBtn element', saveBtn, nameText);
        fromEvent(document.querySelector('#save')!, 'click').subscribe(it => {
            console.log('btn click event-->', it);
            this.ajaxService.postJson('/admin', {name: nameText.value}).subscribe(it => {
                this.loadData()
            })
        })
    }

    get template(): string {
        return html;
    }
}
