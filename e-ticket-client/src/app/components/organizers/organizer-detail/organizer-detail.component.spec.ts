import { ComponentFixture, TestBed } from '@angular/core/testing';

import { OrganizerDetailComponent } from './organizer-detail.component';

describe('OrganizerDetailComponent', () => {
  let component: OrganizerDetailComponent;
  let fixture: ComponentFixture<OrganizerDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ OrganizerDetailComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(OrganizerDetailComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
