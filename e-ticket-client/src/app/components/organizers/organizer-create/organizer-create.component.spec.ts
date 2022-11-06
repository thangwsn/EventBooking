import { ComponentFixture, TestBed } from '@angular/core/testing';

import { OrganizerCreateComponent } from './organizer-create.component';

describe('OrganizerCreateComponent', () => {
  let component: OrganizerCreateComponent;
  let fixture: ComponentFixture<OrganizerCreateComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ OrganizerCreateComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(OrganizerCreateComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
