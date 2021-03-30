import {Sim} from '../../../com/simple/boot/decorators/SimDecorator'
import {Module} from '../../../com/simple/boot/module/Module'
import {AjaxService} from '../../../com/simple/boot/service/AjaxService'
import {ViewService} from '../../../com/simple/boot/service/ViewService'
import html from './hello-world.html'
import css from 'raw-loader!./hello-word.css'
import {Profile} from '../../shareds/Profile'
import {UserResponse} from '@src/app/models/UserResponse'

@Sim()
export class HelloWord extends Module {
    template = html
    styleImports = [css]
    public data: UserResponse | undefined
    public profile: Profile | undefined;
    constructor(public v: ViewService, public ajax: AjaxService) {
        super('hello-world')
    }

    onInit() {
        this.data = undefined;
        this.profile = new Profile();
        this.loadData()
    }

    loadData() {
        this.ajax.getJson<UserResponse>('https://randomuser.me/api/', {name: this.v.eI('name')?.value}).subscribe(it => {
            this.data = it;
            this.profile?.setUser(this.data.results[0])
        })
    }

    onChangedRendered() {
        this.v.eI('save')?.click<Event>().subscribe(it => {
            this.loadData()
        })
    }
}
