import { ComponentFixture, TestBed } from '@angular/core/testing';

import { OrganizerEditComponent } from './organizer-edit.component';

describe('OrganizerEditComponent', () => {
  let component: OrganizerEditComponent;
  let fixture: ComponentFixture<OrganizerEditComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ OrganizerEditComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(OrganizerEditComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
